package pl.poznan.igr.service.stats.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.poznan.igr.domain.BlobFile;
import pl.poznan.igr.domain.Context;
import pl.poznan.igr.domain.StatsSession;
import pl.poznan.igr.domain.UnzipSession;
import pl.poznan.igr.domain.type.Status;
import pl.poznan.igr.service.ServiceImpl;
import pl.poznan.igr.service.router.RouterService;
import pl.poznan.igr.service.stats.RException;
import pl.poznan.igr.service.stats.StatsService;

import com.google.common.io.Files;

// CLEAN up logging mechanisms: slf4j, log4j, *.jars

@Service
public class StatsServiceImpl extends ServiceImpl implements StatsService {

	public static final String OUT_PATH = "target/output";

	@Autowired
	RouterService routerService;

	private final static Logger log = LoggerFactory
			.getLogger(StatsServiceImpl.class);

	@Override
	public void process(Context ctx) {
		calculateStats(ctx);
		routerService.runNext(ctx);
	}

	@Override
	public void calculateStats(Context ctx) {

		UnzipSession us = ctx.getUnzipSession();

		String path = us.getUnzipPath();
		File output = new File(path + "/output");
		output.mkdirs();

		try {
			// TODO wydzieli� statystyki do osobnego watku
			// bo sie dlugo wczytuje
			// dynamiczne stona z lista analiz i odswiezanie stanu

			calculateStats(path);
			ctx.setStatus(Status.ANALYSED);

			// TODO separate -- think of moving to its own service
			// TODO get result file name, set in statsSession and put into DB
			String fname = path + "/output/stats.txt";
			log.info(fname);
			final File f = new File(fname);
			byte[] content = Files.toByteArray(f);

			// CLEAN redesign blob creation, here and import service
			final BlobFile blobFile = new BlobFile();
			blobFile.setContent(content);
			blobFile.setFileName("stats.txt");

			StatsSession ss = new StatsSession();
			ss.setBlobFile(blobFile);
			ss.setContext(ctx);

			ctx.setStatsSession(ss);
			ctx.setStatus(Status.ANALYSED_SAVED);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ctx.setStatus(Status.ANALYSIS_FAILED);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ctx.setStatus(Status.ANALYSIS_FAILED);
		}

	}

	@Override
	public void calculateStats(String fileName) {

		// CLEAN check if R is installed - win/linux

		String rHome = System.getenv("R_HOME");
		if (rHome == null) {
			throw new RException(
					"Environmental variable R_HOME is missing. Analysis failed.");
		}
		String exe = rHome + "/bin/x64/Rscript.exe";
		log.debug("==========\nR exe = " + exe);
		boolean can = new File(exe).canExecute();
		log.debug("can execute = " + can);
		if (!can) {
			throw new RException("Cannot run R executable at " + exe
					+ ". Analysis failed.");
		}

		URL scriptUrl = this.getClass().getClassLoader()
				.getResource("analyse.R");
		if (scriptUrl == null) {
			throw new RException("Couldn't find script analyse.R at "
					+ scriptUrl + ". Analysis failed.");
		}

		String script = scriptUrl.getFile().substring(1);
		log.debug("script = " + script);

		String wd = fileName;
		log.debug("working dir = " + wd);

		try {
			Process p = Runtime.getRuntime().exec(
					new String[] { exe, script, wd });

			p.getErrorStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));
			String line;

			while ((line = br.readLine()) != null) {
				log.debug(line);
			}

			p.waitFor();
			int success = p.exitValue();
			log.info("Process exited with " + success);
			if (success != 0) {

				log.error("Process exited with " + success);
				throw new RException(
						"R analysis failed. Probably there are errors in the processed ISA-TAB file.");
			}
		} catch (IOException e) {
			// TODO handle technical errors - retry and be sorry to the user
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO don't show "Download statistics" if analysis failed
	}

	/*
	 * @Override
	 * 
	 * @Transactional public void calculateStats(String fileName) {
	 * 
	 * //TODO rethink using JRI //cannot be initialized twice //R_HOME, R.dll in
	 * path, jri.dll in path
	 * 
	 * Rengine re = new Rengine (new String[]{"--no-save"}, false, null);
	 * System.out.println("Rengine created, waiting for R");
	 * 
	 * if (!re.waitForR()) { System.out.println("Cannot load R"); return; }
	 * 
	 * re.assign("a", new int[]{36}); REXP ans = re.eval("sqrt(a)"); Double
	 * result = ans.asDouble(); System.err.println("\n\n R call result: " +
	 * result + "\n\n");
	 * 
	 * re.eval("t <- read.table('test.txt')");
	 * re.eval("write(mean(t$V1), file='src//test.out.txt')");
	 * re.eval("write(getwd(), file='test.out.txt', append=T)");
	 * re.eval("sink('test.sink.txt'); print(10); sink()"); ans =
	 * re.eval("mean(t$V1)"); result = ans.asDouble();
	 * System.err.println("\n\n R call result: " + result + "\n\n");
	 * 
	 * 
	 * REXP ans = re.eval(
	 * "source('c://Users//hcwi//Documents//workspace-sts-3.2.0.RELEASE//fileup//src//main//resources//analyse.R')"
	 * ); //re.eval("write(mean(t$V1), file='source.txt');"); // //String
	 * command = "analyse('" + fileName + "')"; //ans = re.eval("mean(1:10)");
	 * System.err.println("\n\n R call result: " + ans + "\n\n"); //re.run();
	 * 
	 * System.err.println(re.eval("getwd()").asString());
	 * 
	 * re.end(); }
	 */

}
