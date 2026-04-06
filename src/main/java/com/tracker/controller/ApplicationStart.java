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
            this.properties = loadProperties("/config.properties");
            Properties cognitoProperties = loadProperties("/cognito.properties");

            ServletContext context = getServletContext();
            context.setAttribute("properties", properties);

            CognitoClientUtil clientUtil = new CognitoClientUtil(cognitoProperties);

            CognitoAuthService cognitoAuth = new CognitoAuthService(cognitoProperties, clientUtil.getClient());
            context.setAttribute("cognitoAuth", cognitoAuth);

            TokenVerifier tokenVerifier = new TokenVerifier(cognitoProperties);
            context.setAttribute("tokenVerifier", tokenVerifier);

        } catch (IOException e) {
            log.error("Issue reading properties file:" + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Problem locating properties file:" + e.getMessage(), e);
        }
    }
}
