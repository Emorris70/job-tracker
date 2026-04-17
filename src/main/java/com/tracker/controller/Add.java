package com.tracker.controller;

import com.tracker.entity.Job;
import com.tracker.entity.User;
import com.tracker.persistence.GenericDao;
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

@WebServlet(urlPatterns = "/add")
public class Add  extends HttpServlet{
    private static final Logger log = LogManager.getLogger(Add.class);
    private GenericDao<Job> jobDao;

    @Override
    public void init() throws ServletException {
        jobDao = new GenericDao<>(Job.class);
    }

    /**
     * Handles the POST request to add a new job.
     *
     * @param req Clients request.
     * @param resp Server response.
     * @throws ServletException If a servlet exception occurs.
     * @throws IOException If an Input/Output exception occurs.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        User user = (User) session.getAttribute("dbUser");

        String company = req.getParameter("companyName");
        String title = req.getParameter("jobTitle");
        String location = req.getParameter("location");
        String salary = req.getParameter("salaryRange");
        String status = req.getParameter("status");
        String dateStr = req.getParameter("dateApplied");
        String url = req.getParameter("jobUrl");
        String description = req.getParameter("description");

        try {
            // Validation check
            if (isInvalid(company)
                    || isInvalid(title)
                    || isInvalid(location)
                    || isInvalid(salary)
                    || isInvalid(dateStr))
            {
                throw new IllegalArgumentException("Please fill in all required fields.");
            }

            LocalDate dateApplied = LocalDate.parse(dateStr);
            Job newjob = new Job(user, company, title, location, salary, status, url, description, dateApplied);
            int insertedId = jobDao.insert(newjob);

            log.info("Job added with ID: {}", insertedId);
            resp.sendRedirect(req.getContextPath() + "/home");

            return;

        } catch (IllegalArgumentException e) {
            log.warn("Validation failed: {}", e.getMessage());
            req.setAttribute("error", e.getMessage());

        } catch (DateTimeParseException e) {
            log.error("Date parsing failed for: {}", dateStr, e);
            req.setAttribute("error", "Invalid date format.");

        } catch (Exception e) {
            log.error("Error adding job: {}", e.getMessage(), e);
            req.setAttribute("error", "An error occurred while adding the job.");
        }

        // redirect to home page
        req.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
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
