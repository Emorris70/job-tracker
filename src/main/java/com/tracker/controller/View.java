package com.tracker.controller;

import com.tracker.entity.ApplicationStatusHistory;
import com.tracker.entity.Job;
import com.tracker.entity.User;
import com.tracker.persistence.ApplicationStatusHistoryDao;
import com.tracker.persistence.JobDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Handles loading a single job application for the detail view.
 *
 * <p>Loads the job by ID, verifies the requesting user owns it, fetches the
 * full status history, and computes the pipeline step data before forwarding
 * to {@code result.jsp}.
 *
 * @author EmileM
 */
@WebServlet(urlPatterns = "/view")
public class View extends BaseServlet {
    private static final Logger log = LogManager.getLogger(View.class);
    private static final List<String> MAIN_STAGES = List.of("Applied", "Screening", "Interview", "Offer");
    private static final Set<String> TERMINAL_STATUSES = Set.of("Rejected", "Withdrawn");
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter STAGE_FMT = DateTimeFormatter.ofPattern("MMM d, yyyy");

    private JobDao jobDao;
    private ApplicationStatusHistoryDao historyDao;

    @Override
    public void init() throws ServletException {
        jobDao = new JobDao();
        historyDao = new ApplicationStatusHistoryDao();
    }

    /**
     * Loads a job by ID and forwards to the detail view with pipeline data.
     *
     * @param req  the client request containing the {@code id} parameter.
     * @param resp the server response.
     * @throws ServletException if a servlet error occurs.
     * @throws IOException      if an I/O error occurs.
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = requireAuth(req, resp);
        if (session == null) return;

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

        List<ApplicationStatusHistory> history = historyDao.findByJobId(jobId);

        req.setAttribute("job", job);
        req.setAttribute("formattedDate", "Applied on " + job.getDateApplied().format(DATE_FMT));
        req.setAttribute("pipelineSteps", buildPipelineSteps(history, job.getStatus()));
        log.info("Loading detail view for job id {}", jobId);
        req.getRequestDispatcher("/WEB-INF/jsp/result.jsp").forward(req, resp);
    }

    /**
     * Builds an ordered list of pipeline step descriptors for the given history.
     * Each step carries its CSS class, entry date, and time spent at that stage.
     * Connector classes between steps are computed based on whether both ends were visited.
     *
     * @param history       chronologically ordered status history for the job.
     * @param currentStatus the job's current status, used to determine active/terminal steps.
     * @return list of step maps consumable by the JSP pipeline bar.
     */
    private List<Map<String, String>> buildPipelineSteps(List<ApplicationStatusHistory> history, String currentStatus) {
        if (currentStatus == null) currentStatus = "Applied";
        boolean isTerminal = TERMINAL_STATUSES.contains(currentStatus);
        LocalDateTime now = LocalDateTime.now();

        Map<String, String[]> historyMap = new LinkedHashMap<>();
        for (int i = 0; i < history.size(); i++) {
            ApplicationStatusHistory entry = history.get(i);

            LocalDateTime end = (i + 1 < history.size()) ? history.get(i + 1).getEnteredAt() : now;

            long days = ChronoUnit.DAYS.between(entry.getEnteredAt(), end);
            historyMap.put(entry.getStatus(), new String[]{
                entry.getEnteredAt().format(STAGE_FMT),
                formatDuration(days)
            });
        }

        List<Map<String, String>> steps = new ArrayList<>();

        for (String stage : MAIN_STAGES) {
            boolean visited = historyMap.containsKey(stage);
            boolean isCurrent = stage.equals(currentStatus) && !isTerminal;

            Map<String, String> step = new LinkedHashMap<>();
            step.put("status", stage);
            step.put("stepClass", isCurrent ? "step-current" : (visited ? "step-done" : "step-upcoming"));
            step.put("enteredAt", visited ? historyMap.get(stage)[0] : "");
            step.put("duration", visited ? historyMap.get(stage)[1] : "");
            steps.add(step);
        }

        if (isTerminal) {
            String[] data = historyMap.get(currentStatus);
            Map<String, String> terminal = new LinkedHashMap<>();

            terminal.put("status", currentStatus);
            terminal.put("stepClass", "step-terminal step-" + currentStatus.toLowerCase());
            terminal.put("enteredAt", data != null ? data[0] : "");
            terminal.put("duration", data != null ? data[1] : "");

            steps.add(terminal);
        }

        for (int i = 0; i < steps.size() - 1; i++) {
            String src = steps.get(i).get("stepClass");
            String dst = steps.get(i + 1).get("stepClass");

            boolean done = src.equals("step-done") && !dst.equals("step-upcoming");
            steps.get(i).put("connectorClass", done ? "connector-done" : "connector-upcoming");
        }

        return steps;
    }

    /**
     * Converts a number of days into a human-readable duration string.
     *
     * @param days elapsed days.
     * @return a string such as "Today", "3 days", "1 week", or "4 weeks".
     */
    private String formatDuration(long days) {
        if (days == 0) return "Today";
        if (days == 1) return "1 day";
        if (days < 7) return days + " days";
        if (days < 14) return "1 week";
        return (days / 7) + " weeks";
    }
}
