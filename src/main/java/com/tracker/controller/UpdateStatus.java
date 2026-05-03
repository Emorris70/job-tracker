package com.tracker.controller;

import com.tracker.entity.Job;
import com.tracker.entity.User;
import com.tracker.persistence.ApplicationStatusHistoryDao;
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
import java.util.Set;

/**
 * Handles status transitions for job applications.
 *
 * <p>Accepts a {@code POST} to {@code /update-status} with {@code jobId} and
 * {@code status} parameters. Validates that the requesting user owns the job,
 * then delegates to {@link ApplicationStatusHistoryDao} to atomically update
 * {@code job.status} and insert a history record. Redirects back to the detail
 * view on success, or to {@code /home} on invalid input.
 *
 * @author EmileM
 */
@WebServlet(urlPatterns = "/update-status")
public class UpdateStatus extends HttpServlet {
    private static final Logger log = LogManager.getLogger(UpdateStatus.class);
    private static final Set<String> VALID_STATUSES = Set.of(
        "Applied", "Screening", "Interview", "Offer", "Rejected", "Withdrawn"
    );

    private JobDao jobDao;
    private ApplicationStatusHistoryDao historyDao;

    @Override
    public void init() throws ServletException {
        jobDao = new JobDao();
        historyDao = new ApplicationStatusHistoryDao();
    }

    /**
     * Validates ownership and transitions the job to the requested status.
     *
     * @param req the client request containing {@code jobId} and {@code status}.
     * @param resp the server response.
     * @throws ServletException if a servlet error occurs.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        String idParam = req.getParameter("jobId");
        String newStatus = req.getParameter("status");

        if (idParam == null || newStatus == null || !VALID_STATUSES.contains(newStatus)) {
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

        User dbUser = (User) session.getAttribute("dbUser");
        Job job = jobDao.getById(jobId);

        if (job == null || job.getUser().getId() != dbUser.getId()) {
            log.warn("User {} attempted to update status of job {} they do not own", dbUser.getId(), jobId);
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        if (newStatus.equals(job.getStatus())) {
            resp.sendRedirect(req.getContextPath() + "/view?id=" + jobId);
            return;
        }

        historyDao.transitionStatus(jobId, newStatus);
        log.info("Status updated to {} for job id {}", newStatus, jobId);
        resp.sendRedirect(req.getContextPath() + "/view?id=" + jobId);
    }
}
