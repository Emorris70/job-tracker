package com.tracker.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet(urlPatterns = "/add")
public class Add  extends HttpServlet{
    private static final Logger log = LogManager.getLogger(Add.class);

    /**
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        HttpSession session = req.getSession(false);

        // get form data
        String companyName = req.getParameter("companyName");
        String Position = req.getParameter("Position");
        String status = req.getParameter("status");
        String dateApplied = req.getParameter("dateApplied");
        String jobUrl = req.getParameter("link");
        String description = req.getParameter("description");


    }
}
