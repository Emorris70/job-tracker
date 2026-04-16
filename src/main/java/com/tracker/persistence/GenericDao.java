package com.tracker.persistence;

import jakarta.persistence.criteria.CriteriaQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.util.List;
import java.util.function.Function;

/**
 * A generic Data Access Object providing common CRUD operations for
 * all entities.
 *
 * @param <T> The type of entity managed by this DAO.
 * @author EmileM
 */
public class GenericDao<T> {
    private static final Logger log = LogManager.getLogger(GenericDao.class);
    private final Class<T> type;

    /**
     * Instantiates a new Generic dao.
     *
     * @param type the entity type, for example, T.
     */
    public GenericDao(Class<T> type) {
        this.type = type;
    }

    /**
     * Returns an open session from the SessionFactory
     *
     * @return the open session
     */
    private Session getSession() {
        return SessionFactoryProvider.getSessionFactory().openSession();
    }

    /**
     * Gets the entity by id and returns the
     * entity corresponding to the id.
     *
     * @param id entity id to search by
     * @return an entity
     */
    public T getById(int id) {
        return executeWithSession(session -> {
            return session.get(type, id);
        });
    }

    /**
     * Insert a new entity and returns an id
     * corresponding to new entry row.
     *
     * @param entity entity to be inserted
     */
    public int insert(T entity) {
        return executeWithSession(session -> {
            session.persist(entity);
            return (int) session.getIdentifier(entity);
        });
    }

    /**
     * Update the entity and returns null.
     *
     * @param entity entity to be updated
     */
    public void update(T entity) {
        executeWithSession(session -> {
            session.merge(entity);
            return null;
        });
    }

    /**
     * Deletes the entity and returns null.
     *
     * @param entity entity to be deleted
     */
    public void delete(T entity) {
        executeWithSession(session -> {
            session.remove(entity);
            return null;
        });
    }

    /**
     * Return a list of all entities
     *
     * @return All entities
     */
    public List<T> getAll() {
        return executeWithSession(session -> {
            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(type);
            query.from(type);
            return session.createSelectionQuery(query).getResultList();
        });
    }

    /**
     * Find entities by a field and value.
     *
     * @param field the field to search by.
     * @param value the value to search for.
     * @return a list of entities
     */
    public List<T> findBy(String field, Object value) {
        return executeWithSession(session -> {
            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(type);
            var root = query.from(type);
            query.where(builder.equal(root.get(field), value));
            return session.createSelectionQuery(query).getResultList();
        });
    }

    /**
     * Wraps a database operation within a Hibernate session and transaction.
     * This method ensures that the transaction is committed on success,
     * rolled back on failure, and the session is always closed.
     *
     * @param action the query to perform
     * @param <R> the return type produced by the action.
     * @return the result in which the calling method would like to return.
     */
    private <R> R executeWithSession(Function<Session, R> action) {
        Session session = getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            R result = action.apply(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
