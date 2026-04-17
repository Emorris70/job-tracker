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
 * Handles forwarding authenticated users to the home page.
 *
 * @author EmileM
 */
@WebServlet(urlPatterns = "/home")
public class Home extends HttpServlet {
    private static final Logger log = LogManager.getLogger(Home.class);

    /**
     * Forwards authenticated users to the home page.
     * <p>
     * Performs a double check accompanied by AuthFilter.
     * Acting as the general protection.
     * <p>
     * AuthFilter (General) -> HomeServlet(Specific) -> home.jsp
     *
     * @param req  Client's Request.
     * @param resp Server's Response.
     * @throws ServletException If a ServletException occurs.
     * @throws IOException      If an Input/Output exception occurs.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        log.info("Authenticated user forward back to home");
        session.setAttribute("page", "Home - Job Tracker");
        req.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
    }
}
