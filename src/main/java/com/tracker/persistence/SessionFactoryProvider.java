package com.tracker.persistence;

import com.tracker.entity.ApplicationStatusHistory;
import com.tracker.entity.Job;
import com.tracker.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
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
            registry = new StandardServiceRegistryBuilder()
                    .applySetting("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                    .applySetting("hibernate.connection.url", "jdbc:" + System.getenv("MYSQL_URL"))
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
            sources.addAnnotatedClass(ApplicationStatusHistory.class);
        } else {
            // Force-load the driver so C3P0's background threads can find it
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                log.error("MySQL JDBC driver not found on classpath", e);
            }

            // Load DB credentials — ${MYSQL_URL} etc. in hibernate.cfg.xml are not
            // substituted automatically, so we apply them explicitly here
            Properties dbProps = new Properties();
            try (InputStream is = SessionFactoryProvider.class.getResourceAsStream("/config.properties")) {
                if (is != null) {
                    dbProps.load(is);
                } else {
                    log.error("config.properties not found on classpath");
                }
            } catch (IOException e) {
                log.error("Failed to load config.properties", e);
            }

            registry = new StandardServiceRegistryBuilder()
                    .configure()
                    .applySetting("hibernate.connection.url",      dbProps.getProperty("MYSQL_URL"))
                    .applySetting("hibernate.connection.username", dbProps.getProperty("MYSQLUSER"))
                    .applySetting("hibernate.connection.password", dbProps.getProperty("MYSQLPASSWORD"))
                    .build();
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