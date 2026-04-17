package com.tracker.persistence;


import com.tracker.entity.Job;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.util.List;

/**
 * Data Access Object for Job entities.
 * @author EmileM
 */
public class JobDao extends GenericDao<Job> {

    /**
     * Initializes a new instance of the JobDao class.
     * Ensuring the generic type is Job.
     */
    public JobDao() {
        super(Job.class);
    }

    /**
     * Finds all jobs by a user.
     * @param userId the user id.
     * @return a list of jobs.
     */
    public List<Job> findByUserId(int userId) {
        return executeWithSession(session -> {
            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Job> query = builder.createQuery(Job.class);
            var root = query.from(Job.class); // you had SavedLocation.class here
            query.where(builder.equal(root.get("user").get("id"), userId));
            return session.createSelectionQuery(query).getResultList();
        });
    }
}
