package com.tracker.controller;

import com.tracker.entity.AuthenticatedUser;
import com.tracker.entity.User;
import com.tracker.persistence.CognitoAuthService;
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

/**
 * Handles the account settings page, including permanent account deletion.
 * The settings page is accessible to authenticated users only.
 *
 * @author EmileM
 */
@WebServlet(urlPatterns = "/settings")
public class AccountSettings extends HttpServlet {
    private static final Logger log = LogManager.getLogger(AccountSettings.class);
    private GenericDao<User> userDao;

    /**
     * Initializes the UserDao used for database operations.
     *
     * @throws ServletException if a servlet exception occurs during initialization.
     */
    @Override
    public void init() throws ServletException {
        userDao = new GenericDao<>(User.class);
    }

    /**
     * Forwards authenticated users to the account settings page.
     * Unauthenticated requests are redirected to the login page.
     *
     * @param req  the client's request.
     * @param resp the server's response.
     * @throws ServletException if a servlet exception occurs.
     * @throws IOException      if an input/output exception occurs.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/jsp/settings.jsp").forward(req, resp);
    }

    /**
     * Handles account settings form submissions.
     * Currently supports the {@code deleteAccount} action, which permanently removes
     * the user from both Cognito and the database, invalidates the session,
     * and redirects to the login page.
     *
     * @param req  the client's request.
     * @param resp the server's response.
     * @throws ServletException if a servlet exception occurs.
     * @throws IOException      if an input/output exception occurs.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        if (!"deleteAccount".equals(req.getParameter("action"))) {
            resp.sendRedirect(req.getContextPath() + "/settings");
            return;
        }

        AuthenticatedUser authUser = (AuthenticatedUser) session.getAttribute("user");
        User dbUser = (User) session.getAttribute("dbUser");

        CognitoAuthService cognitoAuth =
                (CognitoAuthService) getServletContext().getAttribute("cognitoAuth");

        if (cognitoAuth == null) {
            log.error("Cognito service unavailable during account deletion");
            session.setAttribute("settingsError", "Service unavailable. Please try again later.");
            resp.sendRedirect(req.getContextPath() + "/settings");
            return;
        }

        String accessToken = authUser.getAccessToken();
        if (accessToken == null || accessToken.isBlank()) {
            log.error("No access token in session for user {} — cannot delete account", dbUser.getId());
            session.setAttribute("settingsError", "Session has expired. Please log in again to delete your account.");
            resp.sendRedirect(req.getContextPath() + "/settings");
            return;
        }

        try {
            cognitoAuth.deleteUserSelf(accessToken);

            User managed = userDao.getById(dbUser.getId());
            if (managed != null) {
                userDao.delete(managed);
            }

            log.info("Account deleted for user {}", dbUser.getId());
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/index.jsp");

        } catch (Exception e) {
            log.error("Error deleting account for user {}: {}", dbUser.getId(), e.getMessage(), e);
            session.setAttribute("settingsError", "An error occurred while deleting your account. Please try again.");
            resp.sendRedirect(req.getContextPath() + "/settings");
        }
    }
}
