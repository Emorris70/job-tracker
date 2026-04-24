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
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Handles loading a single job application for the result/detail page.
 *
 * @author EmileM
 */
@WebServlet(urlPatterns = "/view")
public class View extends HttpServlet {
    private static final Logger log = LogManager.getLogger(View.class);
    private JobDao jobDao;

    @Override
    public void init() throws ServletException {
        jobDao = new JobDao();
    }

    /**
     * Loads a job by id and forwards to the result page.
     *
     * @param req  Client's Request.
     * @param resp Server's Response.
     * @throws ServletException If a ServletException occurs.
     * @throws IOException      If an Input/Output exception occurs.
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        int jobId;
        try {
            jobId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        Job job = jobDao.getById(jobId);
        User dbUser = (User) session.getAttribute("dbUser");

        if (job == null || job.getUser().getId() != dbUser.getId()) {
            log.warn("User {} attempted to access job {} they do not own", dbUser.getId(), jobId);
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
        String formattedDate = "Applied on " + job.getDateApplied().format(formatter);

        req.setAttribute("job", job);
        req.setAttribute("formattedDate", formattedDate);
        log.info("Loading detail view for job id {}", jobId);
        req.getRequestDispatcher("/WEB-INF/jsp/result.jsp").forward(req, resp);
    }
}
