package com.tracker.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Processes the content of a .properties file
 * @author EmileM
 */
public interface PropertiesLoader {

    Logger log = LogManager.getLogger(PropertiesLoader.class);

    /**
     * Loads properties from environment variables when deployed (Railway),
     * falling back to a classpath file for local development.
     *
     * @param propertiesFilePath classpath path used only in local dev fallback
     * @return populated Properties instance
     */
    default Properties loadProperties(String propertiesFilePath) {
        Properties properties = new Properties();

        if (System.getenv("MYSQL_URL") != null) {
            properties.setProperty("aws.cognito.userPoolId",  System.getenv("AWS_COGNITO_USER_POOL_ID"));
            properties.setProperty("aws.cognito.clientId",    System.getenv("AWS_COGNITO_CLIENT_ID"));
            properties.setProperty("aws.cognito.clientSecret",System.getenv("AWS_COGNITO_CLIENT_SECRET"));
            properties.setProperty("aws.cognito.region",      System.getenv("AWS_COGNITO_REGION"));

            return properties;
        } else {
            try (InputStream is = this.getClass().getResourceAsStream(propertiesFilePath)) {
                if (is != null) {
                    properties.load(is);
                } else {
                    log.error("Properties file not found on classpath: {}", propertiesFilePath);
                }
            } catch (IOException e) {
                log.error("Could not load properties file: {}", propertiesFilePath, e);
            }
        }

        return properties;
    }
}
