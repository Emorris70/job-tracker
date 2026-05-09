package com.tracker.controller;

import com.tracker.entity.Job;
import com.tracker.entity.User;
import com.tracker.persistence.JobDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * Handles forwarding authenticated users to the home page.
 *
 * <p>Beyond loading the user's job list, this servlet computes two sets of
 * derived data before forwarding:
 * <ul>
 *   <li><b>Stats</b> — total applications, active count, interview rate, offer rate.</li>
 *   <li><b>Timeline</b> — per-week application counts for the last 12 weeks,
 *       pre-scaled for a CSS bar chart.</li>
 * </ul>
 *
 * <p>All computation happens here so the JSP remains logic-free.
 *
 * @author EmileM
 */
@WebServlet(urlPatterns = "/home")
public class Home extends BaseServlet {
    private static final Logger log = LogManager.getLogger(Home.class);
    private static final Set<String> TERMINAL = Set.of("Rejected", "Withdrawn");
    private static final Set<String> INTERVIEW_OR_BEYOND = Set.of("Interview", "Interviewing", "Offer");

    private JobDao jobDao;

    @Override
    public void init() throws ServletException {
        jobDao = new JobDao();
    }

    /**
     * Loads the user's job applications and forwards to {@code home.jsp} with
     * stats and timeline attributes set.
     *
     * <p>Performs an ownership double-check alongside {@code AuthFilter}.
     * Flow: {@code AuthFilter} (general) → {@code Home} (specific) → {@code home.jsp}.
     *
     * @param req the client request.
     * @param resp the server response.
     * @throws ServletException if a servlet error occurs.
     * @throws IOException  if an I/O error occurs.
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = requireAuth(req, resp);
        if (session == null) return;

        User dbUser = (User) session.getAttribute("dbUser");
        if (dbUser == null) {
            log.warn("Session missing dbUser — invalidating and redirecting to login");
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        List<Job> jobs = jobDao.findByUserId(dbUser.getId());

        req.setAttribute("jobs", jobs);
        setStats(req, jobs);

        log.info("Forwarding user {} to home with {} jobs", dbUser.getId(), jobs.size());
        session.setAttribute("page", "Home - Job Tracker");
        req.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
    }

    /**
     * Computes summary statistics from the job list and sets them as individual
     * request attributes ({@code statTotal}, {@code statActive},
     * {@code statInterviewRate}, {@code statOfferRate}).
     *
     * <p>Interview rate counts jobs currently at the Interview or Offer stage.
     * Offer rate counts jobs currently at Offer. Both return {@code "—"} when
     * the total is zero.
     *
     * @param req the request to set attributes on.
     * @param jobs the user's full job list.
     */
    private void setStats(HttpServletRequest req, List<Job> jobs) {
        int total = jobs.size();
        int active = 0, atInterview = 0, atOffer = 0;

        for (Job job : jobs) {
            String s = job.getStatus();
            if (!TERMINAL.contains(s)) active++;
            if (INTERVIEW_OR_BEYOND.contains(s)) atInterview++;
            if ("Offer".equals(s)) atOffer++;
        }

        req.setAttribute("statTotal", total);
        req.setAttribute("statActive", active);
        req.setAttribute("statInterviewRate",
                total > 0 ? Math.round(atInterview * 100.0 / total) + "%" : "—");
        req.setAttribute("statOfferRate",
                total > 0 ? Math.round(atOffer * 100.0 / total) + "%" : "—");
    }

}
