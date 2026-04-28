package com.tracker.controller;

import com.tracker.entity.AuthenticatedUser;
import com.tracker.entity.User;
import com.tracker.persistence.CognitoAuthService;
import com.tracker.persistence.GenericDao;
import com.tracker.persistence.TokenVerifier;
import jakarta.servlet.RequestDispatcher;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.io.IOException;

/**
 * This authorization class manages end-user forwarding/redirection, handles
 * new user creation forms, and validates specific user credentials.
 *
 * @author EmileM
 */

@WebServlet(
        urlPatterns = {"/auth"}
)
public class Auth extends HttpServlet {
    private final Logger log = LogManager.getLogger(this.getClass());

    /**
     * Forwards the end-user to the desired page.
     * This action is triggered through an anchor tag
     *
     * @param req Client's Request.
     * @param resp Server's Response.
     * @throws ServletException If a ServletException occurs.
     * @throws IOException If a Input/Output exception occurs.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        HttpSession session = req.getSession(false);

        if (session != null) {
            session.removeAttribute("error");
        }

        String url = "";

        if ("sign-up".equals(req.getParameter("action"))) {
            url = "/signup.jsp";
            req.setAttribute("page" ,"Sign up - Job Tracker");

        } else if ("login".equals(req.getParameter("action"))) {
            url = "/index.jsp";
            req.setAttribute("page", "Login - Job Tracker");

        } else if ("reset-pass".equals(req.getParameter("action"))) {
            url = "/resetPassword.jsp";
            req.setAttribute("page", "Reset Password - Job Tracker");

        } else if ("reset-pass-confirm".equals(req.getParameter("action"))) {
            url = "/resetPasswordConfirm.jsp";
            req.setAttribute("page", "Reset Password - Job Tracker");

        } else if ("resendCode".equals(req.getParameter("action"))) {
            String email = session != null ? (String) session.getAttribute("pendingConfirmEmail") : null;

            if (email == null || email.isBlank()) {
                if (session != null) session.setAttribute("error", "Session expired. Please sign up again.");
                resp.sendRedirect("signup.jsp");
                return;
            }

            CognitoAuthService cognitoAuth = (CognitoAuthService) getServletContext().getAttribute("cognitoAuth");
            try {
                cognitoAuth.resendConfirmationCode(email);
                session.setAttribute("successMsg", "A new code has been sent to your email");
            } catch (TooManyRequestsException e) {
                session.setAttribute("error", "Too many attempts, please try again later");
            } catch (Exception e) {
                log.error("Error resending confirmation code: {}", e.getMessage(), e);
                session.setAttribute("error", "Something went wrong please try again");
            }
            resp.sendRedirect("confirm.jsp");
            return;
        }

        if (url.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher(url);
        dispatcher.forward(req, resp);
    }

    /**
     * Handles all form submissions for authentication.
     * Manages user registration, confirmation, and login.
     *
     * @param req Client's Request.
     * @param resp Server's Response
     * @throws ServletException If a ServletException occurs.
     * @throws IOException If a Input/Output exception occurs.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        HttpSession session = req.getSession();
        session.removeAttribute("error");

        CognitoAuthService cognitoAuth = (CognitoAuthService) getServletContext().getAttribute("cognitoAuth");
        TokenVerifier tokenVerifier = (TokenVerifier) getServletContext().getAttribute("tokenVerifier");

        if (cognitoAuth == null || tokenVerifier == null) {
            log.error("Auth services not initialized — ApplicationStart may have failed");
            session.setAttribute("error", "Service unavailable. Please try again later.");
            resp.sendRedirect("index.jsp");
            return;
        }

        String action = req.getParameter("action");

        if (action == null || action.isBlank()) {
            resp.sendRedirect("index.jsp");
            return;
        }

        if ("signUp".equals(action)) {

            String firstName = req.getParameter("first_name");
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            if (firstName == null || firstName.isBlank() ||
                email == null || email.isBlank() ||
                password == null || password.isBlank()) {
                session.setAttribute("error", "Please fill out all required fields");
                resp.sendRedirect("signup.jsp");
                return;
            }

            try {
                String sub = cognitoAuth.register(firstName, email, password);

                session.setAttribute("pendingConfirmEmail", email);
                session.setAttribute("pendingConfirmSub", sub);
                session.setAttribute("title", "confirm - Job Tracker");
                resp.sendRedirect("confirm.jsp");

            } catch (UsernameExistsException e) {
                session.setAttribute("error", "An account with this email already exists");
                resp.sendRedirect("signup.jsp");

            } catch (InvalidPasswordException e) {
                session.setAttribute("error", "Password does not meet requirements");
                resp.sendRedirect("signup.jsp");

            } catch (InvalidParameterException e) {
                session.setAttribute("error", "Please ensure all fields are filled out correctly");
                resp.sendRedirect("signup.jsp");

            } catch (TooManyRequestsException e) {
                session.setAttribute("error", "Too many attempts please try again later");
                resp.sendRedirect("signup.jsp");

            } catch (Exception e) {
                log.error("Error registering user: {}", e.getMessage(), e);
                session.setAttribute("error", "Something went wrong please try again");
                resp.sendRedirect("signup.jsp");

            }

        } else if ("confirm".equals(action)) {
            String email = (String) session.getAttribute("pendingConfirmEmail");
            String code = req.getParameter("v-code");

            String pendingSub = (String) session.getAttribute("pendingConfirmSub");

            if (email == null || email.isBlank() || pendingSub == null || pendingSub.isBlank()) {
                session.setAttribute("error", "Session expired. Please sign up again.");
                resp.sendRedirect("signup.jsp");
                return;
            }
            if (code == null || code.isBlank()) {
                session.setAttribute("error", "Please enter your verification code");
                resp.sendRedirect("confirm.jsp");
                return;
            }

            try {
                cognitoAuth.confirmSignUp(email, code);

                GenericDao<User> userDao = new GenericDao<>(User.class);
                userDao.insert(new User(pendingSub));

                session.removeAttribute("pendingConfirmEmail");
                session.removeAttribute("pendingConfirmSub");
                resp.sendRedirect("index.jsp");

            } catch (CodeMismatchException e) {
                session.setAttribute("error", "Invalid verification code");
                resp.sendRedirect("confirm.jsp");

            } catch (ExpiredCodeException e) {

                session.setAttribute("error", "Code has expired please request a new one");
                resp.sendRedirect("confirm.jsp");

            } catch (Exception e) {
                log.error("Error confirming user: {}", e.getMessage(), e);
                session.setAttribute("error", "Something went wrong please try again");
                resp.sendRedirect("confirm.jsp");

            }
        } else if ("login".equals(action)) {
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            if (email == null || email.isBlank() ||
                password == null || password.isBlank()) {
                session.setAttribute("error", "Please enter your email and password");
                resp.sendRedirect("index.jsp");
                return;
            }

            try {

                AuthenticationResultType result = cognitoAuth.login(email, password);
                AuthenticatedUser authUser = tokenVerifier.verify(result.idToken());

                GenericDao<User> userDao = new GenericDao<>(User.class);
                java.util.List<User> users = userDao.findBy("sub", authUser.getSub());
                if (users.isEmpty()) {
                    log.error("Authenticated Cognito user has no matching DB record: {}", authUser.getSub());
                    session.setAttribute("error", "Account setup incomplete. Please contact support.");
                    resp.sendRedirect("index.jsp");
                    return;
                }

                // Session fixation prevention: invalidate old session and create fresh one
                session.invalidate();
                HttpSession newSession = req.getSession(true);
                newSession.setAttribute("user", authUser);
                newSession.setAttribute("dbUser", users.get(0));

                resp.sendRedirect(req.getContextPath() + "/home");

            } catch (NotAuthorizedException e) {
                session.setAttribute("error", "Incorrect email or password");
                resp.sendRedirect("index.jsp");

            } catch (UserNotConfirmedException e) {
                session.setAttribute("error", "Please confirm your email before logging in");
                resp.sendRedirect("index.jsp");

            } catch (UserNotFoundException e) {
                session.setAttribute("error", "No account found with that email");
                resp.sendRedirect("index.jsp");

            } catch (Exception e) {
                log.error("Error logging in user: {}", e.getMessage(), e);
                session.setAttribute("error", "Something went wrong please try again");
                resp.sendRedirect("index.jsp");

            }
        } else if ("forgotPassword".equals(action)) {
            String email = req.getParameter("email");

            if (email == null || email.isBlank()) {
                session.setAttribute("error", "Please enter your email address");
                resp.sendRedirect("resetPassword.jsp");
                return;
            }

            try {
                cognitoAuth.forgotPassword(email);
                session.setAttribute("resetEmail", email);
                resp.sendRedirect("auth?action=reset-pass-confirm");

            } catch (UserNotFoundException e) {
                session.setAttribute("error", "No account found with that email");
                resp.sendRedirect("resetPassword.jsp");

            } catch (InvalidParameterException e) {
                session.setAttribute("error", "Account not confirmed. Please verify your email first");
                resp.sendRedirect("resetPassword.jsp");

            } catch (TooManyRequestsException e) {
                session.setAttribute("error", "Too many attempts, please try again later");
                resp.sendRedirect("resetPassword.jsp");

            } catch (Exception e) {
                log.error("Error resetting password: {}", e.getMessage(), e);
                session.setAttribute("error", "Something went wrong please try again");
                resp.sendRedirect("resetPassword.jsp");

            }
        } else if ("confirmForgotPassword".equals(action)) {
            String email = (String) session.getAttribute("resetEmail");
            String code = req.getParameter("v-code");
            String newPassword = req.getParameter("password");

            if (email == null || email.isBlank()) {
                session.setAttribute("error", "Session expired. Please restart the password reset.");
                resp.sendRedirect("resetPassword.jsp");
                return;
            }
            if (code == null || code.isBlank() || newPassword == null || newPassword.isBlank()) {
                session.setAttribute("error", "Please fill out all required fields");
                resp.sendRedirect("resetPasswordConfirm.jsp");
                return;
            }

            try {
                cognitoAuth.confirmForgotPassword(email, code, newPassword);
                session.removeAttribute("resetEmail");
                session.setAttribute("successMsg", "Password reset successfully. Please log in.");
                resp.sendRedirect("index.jsp");

            } catch (CodeMismatchException e) {
                session.setAttribute("error", "Invalid verification code");
                resp.sendRedirect("resetPasswordConfirm.jsp");

            } catch (ExpiredCodeException e) {
                session.setAttribute("error", "Code has expired, please request a new one");
                resp.sendRedirect("resetPassword.jsp");

            } catch (InvalidPasswordException e) {
                session.setAttribute("error", "Password does not meet requirements");
                resp.sendRedirect("resetPasswordConfirm.jsp");

            } catch (TooManyRequestsException e) {
                session.setAttribute("error", "Too many attempts, please try again later");
                resp.sendRedirect("resetPasswordConfirm.jsp");

            } catch (Exception e) {
                log.error("Error confirming forgotten password: {}", e.getMessage(), e);
                session.setAttribute("error", "Something went wrong please try again");
                resp.sendRedirect("resetPasswordConfirm.jsp");

            }
        }

    }

}
