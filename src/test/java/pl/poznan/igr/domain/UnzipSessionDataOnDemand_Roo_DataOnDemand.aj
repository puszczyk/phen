// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package pl.poznan.igr.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect UnzipSessionDataOnDemand_Roo_DataOnDemand {
    
    declare @type: UnzipSessionDataOnDemand: @Component;
    
    private Random UnzipSessionDataOnDemand.rnd = new SecureRandom();
    
    private List<UnzipSession> UnzipSessionDataOnDemand.data;
    
    @Autowired
    ContextDataOnDemand UnzipSessionDataOnDemand.contextDataOnDemand;
    
    public UnzipSession UnzipSessionDataOnDemand.getNewTransientUnzipSession(int index) {
        UnzipSession obj = new UnzipSession();
        setContext(obj, index);
        setCreationDate(obj, index);
        setUnzipPath(obj, index);
        return obj;
    }
    
    public void UnzipSessionDataOnDemand.setContext(UnzipSession obj, int index) {
        Context context = contextDataOnDemand.getSpecificContext(index);
        obj.setContext(context);
    }
    
    public void UnzipSessionDataOnDemand.setCreationDate(UnzipSession obj, int index) {
        Date creationDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreationDate(creationDate);
    }
    
    public void UnzipSessionDataOnDemand.setUnzipPath(UnzipSession obj, int index) {
        String unzipPath = "unzipPath_" + index;
        obj.setUnzipPath(unzipPath);
    }
    
    public UnzipSession UnzipSessionDataOnDemand.getSpecificUnzipSession(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        UnzipSession obj = data.get(index);
        Long id = obj.getId();
        return UnzipSession.findUnzipSession(id);
    }
    
    public UnzipSession UnzipSessionDataOnDemand.getRandomUnzipSession() {
        init();
        UnzipSession obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return UnzipSession.findUnzipSession(id);
    }
    
    public boolean UnzipSessionDataOnDemand.modifyUnzipSession(UnzipSession obj) {
        return false;
    }
    
    public void UnzipSessionDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = UnzipSession.findUnzipSessionEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'UnzipSession' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<UnzipSession>();
        for (int i = 0; i < 10; i++) {
            UnzipSession obj = getNewTransientUnzipSession(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}
