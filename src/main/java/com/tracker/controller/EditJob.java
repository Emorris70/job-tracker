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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Handles loading and saving edits to a job application.
 *
 * @author EmileM
 */
@WebServlet(urlPatterns = "/edit")
public class EditJob extends HttpServlet {
    private static final Logger log = LogManager.getLogger(EditJob.class);
    private JobDao jobDao;

    @Override
    public void init() throws ServletException {
        jobDao = new JobDao();
    }

    /**
     * Loads the job and forwards to the edit form.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        Job job = resolveOwnedJob(req, resp, session);
        if (job == null) return;

        req.setAttribute("job", job);
        req.getRequestDispatcher("/WEB-INF/jsp/edit.jsp").forward(req, resp);
    }

    /**
     * Validates and saves the edited job, then redirects to the detail page.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        Job job = resolveOwnedJob(req, resp, session);
        if (job == null) return;

        String company  = req.getParameter("companyName");
        String title    = req.getParameter("jobTitle");
        String location = req.getParameter("location");
        String salary   = req.getParameter("salaryRange");
        String status   = req.getParameter("status");
        String dateStr  = req.getParameter("dateApplied");
        String url      = req.getParameter("jobUrl");
        String description = req.getParameter("description");

        try {
            if (isInvalid(company) || isInvalid(title) || isInvalid(location)
                    || isInvalid(salary) || isInvalid(dateStr)) {
                throw new IllegalArgumentException("Please fill out all required fields.");
            }
            if (!salary.matches(".*\\d.*")) {
                throw new IllegalArgumentException("Salary range must contain a numeric value.");
            }
            if (!isInvalid(url) && !url.matches("^https?://.*")) {
                throw new IllegalArgumentException("Job link must start with http:// or https://");
            }

            job.setCompanyName(company);
            job.setJobTitle(title);
            job.setLocation(location);
            job.setSalaryRange(salary);
            job.setStatus(status);
            job.setDateApplied(LocalDate.parse(dateStr));
            job.setJobUrl(url);
            job.setDescription(description);

            jobDao.update(job);
            log.info("Job {} updated by user {}", job.getId(), ((User) session.getAttribute("dbUser")).getId());
            resp.sendRedirect(req.getContextPath() + "/view?id=" + job.getId());

        } catch (IllegalArgumentException e) {
            log.warn("Edit validation failed: {}", e.getMessage());
            session.setAttribute("editError", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/edit?id=" + job.getId());

        } catch (DateTimeParseException e) {
            session.setAttribute("editError", "Invalid date format.");
            resp.sendRedirect(req.getContextPath() + "/edit?id=" + job.getId());

        } catch (Exception e) {
            log.error("Error updating job {}: {}", job.getId(), e.getMessage(), e);
            session.setAttribute("editError", "An error occurred while saving changes.");
            resp.sendRedirect(req.getContextPath() + "/edit?id=" + job.getId());
        }
    }

    /**
     * Resolves the job to be edited, ensuring the user owns it.
     * @param req Clients request.
     * @param resp Server response.
     * @param session Current user session.
     * @return The job to be edited or null if the user does not own the job.
     * @throws IOException If an Input/Output exception occurs.
     */
    private Job resolveOwnedJob(HttpServletRequest req, HttpServletResponse resp,
                                HttpSession session) throws IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return null;
        }
        int jobId;
        try {
            jobId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return null;
        }

        Job job = jobDao.getById(jobId);
        User dbUser = (User) session.getAttribute("dbUser");

        if (job == null || job.getUser().getId() != dbUser.getId()) {
            log.warn("User {} attempted to access job {} they do not own", dbUser.getId(), jobId);
            resp.sendRedirect(req.getContextPath() + "/home");
            return null;
        }
        return job;
    }

    /**
     * Checks if the value is null or empty.
     * @param value the value to check.
     * @return true if the value is null or empty, false otherwise.
     */
    private boolean isInvalid(String value) {
        return value == null || value.trim().isEmpty();
    }
}
