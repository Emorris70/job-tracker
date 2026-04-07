package com.tracker.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Handles user logout by invalidating the session
 * and redirecting to the login page.
 *
 * @author EmileM
 */
@WebServlet(urlPatterns = "/logout")
public class Logout extends HttpServlet {
    private static final Logger log = LogManager.getLogger(Logout.class);

    /**
     * Invalidates the user session and redirects to the login page.
     *
     * @param req Client's Request.
     * @param resp Server's Response.
     * @throws ServletException If a ServletException occurs.
     * @throws IOException If an Input/Output exception occurs.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session != null) {
            log.info("User logged out: " + session.getAttribute("email"));
            session.invalidate();
        }

        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }
}