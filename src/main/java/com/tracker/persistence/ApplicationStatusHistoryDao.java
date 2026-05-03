package com.tracker.persistence;

import com.tracker.entity.ApplicationStatusHistory;
import com.tracker.entity.Job;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.util.List;

/**
 * Data Access Object for {@link ApplicationStatusHistory} entities.
 * Handles querying and recording status transitions for job applications.
 *
 * @author EmileM
 */
public class ApplicationStatusHistoryDao extends GenericDao<ApplicationStatusHistory> {

    /**
     * Initializes the DAO with {@link ApplicationStatusHistory} as the entity type.
     */
    public ApplicationStatusHistoryDao() {
        super(ApplicationStatusHistory.class);
    }

    /**
     * Returns all status history records for a given job, ordered chronologically.
     *
     * @param jobId the ID of the job application.
     * @return ordered list of status history entries.
     */
    public List<ApplicationStatusHistory> findByJobId(int jobId) {
        return executeWithSession(session -> {
            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<ApplicationStatusHistory> query = builder.createQuery(ApplicationStatusHistory.class);
            var root = query.from(ApplicationStatusHistory.class);
            query.where(builder.equal(root.get("job").get("id"), jobId));
            query.orderBy(builder.asc(root.get("enteredAt")));
            return session.createSelectionQuery(query).getResultList();
        });
    }

    /**
     * Transitions a job to a new status by updating {@code job.status} and inserting
     * a history record within a single transaction.
     *
     * @param jobId     the ID of the job to update.
     * @param newStatus the status to transition to.
     */
    public void transitionStatus(int jobId, String newStatus) {
        executeWithSession(session -> {
            Job job = session.get(Job.class, jobId);
            job.setStatus(newStatus);
            session.merge(job);
            session.persist(new ApplicationStatusHistory(job, newStatus));
            return null;
        });
    }

    /**
     * Records the initial status of a newly created job application.
     * Should be called immediately after job insertion.
     *
     * @param jobId  the ID of the newly created job.
     * @param status the initial status to record.
     */
    public void recordInitialStatus(int jobId, String status) {
        executeWithSession(session -> {
            Job jobRef = session.getReference(Job.class, jobId);
            session.persist(new ApplicationStatusHistory(jobRef, status));
            return null;
        });
    }
}
