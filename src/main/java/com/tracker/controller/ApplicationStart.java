package com.tracker.controller;

import com.tracker.persistence.*;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Initializes the application with essential properties
 * values.
 *
 * @author EmileM
 */
@WebServlet(
        urlPatterns = "/appStart",
        loadOnStartup = 1
)
public class ApplicationStart extends HttpServlet implements PropertiesLoader {
    private static final Logger log = LogManager.getLogger(ApplicationStart.class);
    private Properties properties;

    /**
     * Stores read properties context and passes the values
     * to the Resources class.
     *
     * @throws ServletException if a servlet exception occurs.
     */
    @Override
    public void init() throws ServletException {
        try {
            Properties cognitoProperties = loadProperties("/cognito.properties");

            ServletContext context = getServletContext();

            CognitoClientUtil clientUtil = new CognitoClientUtil(cognitoProperties);

            CognitoAuthService cognitoAuth = new CognitoAuthService(cognitoProperties, clientUtil.getClient());
            context.setAttribute("cognitoAuth", cognitoAuth);

            TokenVerifier tokenVerifier = new TokenVerifier(cognitoProperties);
            context.setAttribute("tokenVerifier", tokenVerifier);

        } catch (Exception e) {
            log.error("Failed to initialize auth services: {}", e.getMessage(), e);
        }
    }
}
