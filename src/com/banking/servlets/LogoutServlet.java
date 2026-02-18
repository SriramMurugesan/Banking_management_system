package com.banking.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 * LogoutServlet - Handles user logout
 */
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Invalidate session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Redirect to login page
        response.sendRedirect("login.html");
    }
}
