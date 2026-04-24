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

/**
 * Handles deleting a job application.
 *
 * @author EmileM
 */
@WebServlet(urlPatterns = "/delete")
public class Delete extends HttpServlet {
    private static final Logger log = LogManager.getLogger(Delete.class);
    private JobDao jobDao;

    @Override
    public void init() throws ServletException {
        jobDao = new JobDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
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
            log.warn("User {} attempted to delete job {} they do not own", dbUser.getId(), jobId);
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        jobDao.delete(job);
        log.info("Job {} deleted by user {}", jobId, dbUser.getId());
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}
