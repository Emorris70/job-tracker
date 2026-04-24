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

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

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
     * Create session factory.
     */
    public static void createSessionFactory() {

        MetadataSources sources;

        if (System.getenv("MYSQL_URL") != null) {
            log.info("Railway environment detected. Initializing Hibernate via Env Vars.");
            registry = new StandardServiceRegistryBuilder()
                    .applySetting("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                    .applySetting("hibernate.connection.url",          System.getenv("MYSQL_URL"))
                    .applySetting("hibernate.connection.username",     System.getenv("MYSQLUSER"))
                    .applySetting("hibernate.connection.password",     System.getenv("MYSQLPASSWORD"))
                    .applySetting("hibernate.dialect",                 "org.hibernate.dialect.MySQLDialect")
                    .applySetting("hibernate.hbm2ddl.auto",            "update")
                    .applySetting("show_sql",                          "false")
                    .applySetting("hibernate.c3p0.min_size",           "5")
                    .applySetting("hibernate.c3p0.max_size",           "20")
                    .applySetting("hibernate.c3p0.timeout",            "300")
                    .applySetting("hibernate.c3p0.max_statements",     "50")
                    .applySetting("hibernate.c3p0.idle_test_period",   "3000")
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