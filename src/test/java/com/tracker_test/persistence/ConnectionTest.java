package com.tracker_test.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Test the connection to a railway database.
 */
public class ConnectionTest {
    /**
     * Test the connection to the database.
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        try {
            // This forces Hibernate to initialize the SessionFactory
            // and check the database schema
            SessionFactory factory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();

            System.out.println("Connection Successful!");

            // Cleanup
            factory.close();
        } catch (Exception e) {
            System.err.println("Connection Failed!");
            e.printStackTrace();
        }
    }
}
