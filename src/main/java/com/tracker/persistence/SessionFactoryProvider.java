package com.tracker.persistence;

import com.tracker.entity.Job;
import com.tracker.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * This file provides a SessionFactory for use with DAOs using Hibernate
 *
 * @author paulawaite
 * @version 3.0
 */
public class SessionFactoryProvider {

    private static SessionFactory sessionFactory;
    private static StandardServiceRegistry registry;
    private static final Logger log = LogManager.getLogger(SessionFactoryProvider.class);

    /**
     * Create a session factory.
     */
    public static void createSessionFactory() {

        MetadataSources sources;

        if (System.getenv("AWS_COGNITO_REGION") != null) {
            log.info("Railway Environment Detected. Connecting to: " + System.getenv("MYSQL_URL"));
            registry = new StandardServiceRegistryBuilder()
                    // 1. Let Hibernate auto-detect the driver from the URL
                    .applySetting("hibernate.connection.url", System.getenv("MYSQL_URL"))
                    .applySetting("hibernate.connection.username", System.getenv("MYSQLUSER"))
                    .applySetting("hibernate.connection.password", System.getenv("MYSQLPASSWORD"))

                    // 2. Use the updated Dialect name as suggested by the logs
                    .applySetting("hibernate.dialect","org.hibernate.dialect.MySQLDialect")

                    // 3. Keep these for Railway's schema management
                    .applySetting("hibernate.hbm2ddl.auto","update")
                    .applySetting("show_sql","false")

                    // 4. Temporarily simplify C3P0 to rule out pool timeouts
                    .applySetting("hibernate.c3p0.min_size", "1")
                    .applySetting("hibernate.c3p0.max_size",  "5")
                    .build();
            sources = new MetadataSources(registry);
            sources.addAnnotatedClass(User.class);
            sources.addAnnotatedClass(Job.class);
        } else {
            // Local dev: use hibernate.cfg.xml
            registry = new StandardServiceRegistryBuilder().configure().build();
            sources = new MetadataSources(registry);
        }

        Metadata metadata = sources.getMetadataBuilder().build();
        sessionFactory = metadata.getSessionFactoryBuilder().build();
    }

    /**
     * Gets session factory.
     *
     * @return the session factory
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            createSessionFactory();
        }
        return sessionFactory;

    }
}