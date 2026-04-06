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
     * This default method will load a properties file into a Properties instance
     * and return it.
     *
     * @param propertiesFilePath a path to a file on the java classpath list
     * @return a populated Properties instance or an empty Properties instance if
     * the file path was not found.
     */
    default Properties loadProperties(String propertiesFilePath) throws IOException, Exception{
        Properties properties = new Properties();
        properties.load(this.getClass().getResourceAsStream(propertiesFilePath));

        return properties;
    }
}
