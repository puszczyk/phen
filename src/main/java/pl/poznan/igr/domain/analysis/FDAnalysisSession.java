package pl.poznan.igr.domain.analysis;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import pl.poznan.igr.domain.Context;

@Entity
@Configurable
public class FDAnalysisSession {

	private static final String STATS_SESSION_FOR_CONTEXT_QUERY = "SELECT z FROM FDAnalysisSession z join z.context c "
			+ "WHERE c.id = :contextId";

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date creationDate;

	@NotNull
	@OneToOne
	private Context context;

    private String message;

    @Enumerated
    private AnalysisStatus status;

	public FDAnalysisSession() {
		this.setCreationDate(new Date());
	}

	public static FDAnalysisSession findStatsSessionForContext(Context context) {
		checkNotNull(context);
		Query query = entityManager().createQuery(
				STATS_SESSION_FOR_CONTEXT_QUERY);
		query.setParameter("contextId", context.getId());
		return (FDAnalysisSession) query.getSingleResult();
	}

	public String toString() {

		String s = this.getClass() + ": id " + this.getId() + " date "
				+ this.getCreationDate() + " message = " + this.getMessage();
		return s;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	public Date getCreationDate() {
        return this.creationDate;
    }

	public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

	public Context getContext() {
        return this.context;
    }

	public void setContext(Context context) {
        this.context = context;
    }

	public String getMessage() {
        return this.message;
    }

	public void setMessage(String message) {
        this.message = message;
    }

    public AnalysisStatus getStatus() {
        return status;
    }

    public void setStatus(AnalysisStatus status) {
        this.status = status;
    }

    public boolean isError() {
        return status != null && status.equals(AnalysisStatus.ERROR);
    }

    @PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("STATS_SESSION_FOR_CONTEXT_QUERY", "creationDate", "context", "message", "status");

	public static final EntityManager entityManager() {
        EntityManager em = new FDAnalysisSession().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countStatsSessions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM FDAnalysisSession o", Long.class).getSingleResult();
    }

	public static List<FDAnalysisSession> findAllStatsSessions() {
        return entityManager().createQuery("SELECT o FROM FDAnalysisSession o", FDAnalysisSession.class).getResultList();
    }

	public static List<FDAnalysisSession> findAllStatsSessions(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM FDAnalysisSession o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, FDAnalysisSession.class).getResultList();
    }

	public static FDAnalysisSession findStatsSession(Long id) {
        if (id == null) return null;
        return entityManager().find(FDAnalysisSession.class, id);
    }

	public static List<FDAnalysisSession> findStatsSessionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM FDAnalysisSession o", FDAnalysisSession.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<FDAnalysisSession> findStatsSessionEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM FDAnalysisSession o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, FDAnalysisSession.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            FDAnalysisSession attached = FDAnalysisSession.findStatsSession(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public FDAnalysisSession merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        FDAnalysisSession merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}