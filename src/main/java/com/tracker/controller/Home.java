package com.tracker.controller;

import com.tracker.entity.Job;
import com.tracker.entity.User;
import com.tracker.persistence.JobDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Handles forwarding authenticated users to the home page.
 *
 * @author EmileM
 */
@WebServlet(urlPatterns = "/home")
public class Home extends HttpServlet {
    private static final Logger log = LogManager.getLogger(Home.class);
    private JobDao jobDao;

    @Override
    public void init() throws ServletException {
        jobDao = new JobDao();
    }

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
            log.warn("Unauthorized access attempt to home page.");
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        User dbUser = (User) session.getAttribute("dbUser");
        if (dbUser == null) {
            log.warn("Session missing dbUser — invalidating and redirecting to login");
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }
        List<Job> jobs = jobDao.findByUserId(dbUser.getId());
        req.setAttribute("jobs", jobs);

        log.info("Authenticated user forward back to home");
        session.setAttribute("page", "Home - Job Tracker");

        req.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
    }
}
