package com.tracker.controller;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Intercepts all incoming requests and ensures
 * the user has a valid session before accessing
 * protected routes.
 *
 * @author EmileM
 */
@WebFilter("/*")
public class AuthFilter implements Filter {
    private static final Logger log = LogManager.getLogger(AuthFilter.class);

    /**
     * Checks for a valid session on every request.
     * Public routes are whitelisted and always accessible.
     * All other routes require a valid session.
     *
     * @param request  Client's request.
     * @param response Server's response.
     * @param chain    The filter chain.
     * @throws ServletException If a servlet exception occurs.
     * @throws IOException If an Input/Output exception occurs.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);

        String path = req.getRequestURI();

        // Public routes are always accessible
        boolean isPublic = path.contains("/index.jsp") ||
                path.contains("/signup.jsp") ||
                path.contains("/confirm.jsp") ||
                path.contains("/logout") ||
                path.contains("/auth") ||
                path.contains("/css") ||
                path.contains("/images") ||
                path.contains("/js");

        HttpSession session = req.getSession(false);

        if (isPublic) {

            if (path.contains("/index.jsp") ||
                    path.contains("/signup.jsp") ||
                    path.contains("/confirm.jsp") ||
                    path.contains("/auth")) {

                if (session != null && session.getAttribute("user") != null) {
                    log.info("Authenticated user redirected back to home");
                    resp.sendRedirect(req.getContextPath() + "/home");
                    return;
                }
            }

            chain.doFilter(request, response);

            return;
        }

        if (session == null || session.getAttribute("user") == null) {
            log.warn("Unauthenticated access attempt to: " + path);
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        log.info("Authenticated access to: " + path);
        chain.doFilter(request, response);
    }
}