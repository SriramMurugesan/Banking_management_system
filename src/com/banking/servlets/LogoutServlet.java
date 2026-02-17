package com.banking.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 * LogoutServlet - Demonstrates SendRedirect
 * Shows the difference between sendRedirect and RequestDispatcher
 */
public class LogoutServlet extends HttpServlet {

    /**
     * doGet() - Handle logout and demonstrate sendRedirect
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("[LogoutServlet] Processing logout with sendRedirect()");

        // Clear session if exists
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            System.out.println("[LogoutServlet] Session invalidated");
        }

        // Use sendRedirect to redirect to home page
        // This is CLIENT-SIDE redirect
        // URL in browser WILL change
        // A new request is created
        System.out.println("[LogoutServlet] Redirecting to index.html using response.sendRedirect()");

        // Add a parameter to show we redirected
        response.sendRedirect("index.html?logout=success");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
