package com.banking.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import com.banking.dao.UserDAO;
import com.banking.model.User;
import com.banking.model.Admin;

/**
 * LoginServlet - Handles user and admin authentication
 */
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType"); // "user" or "admin"

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (userType.equals("admin")) {
            // Admin login
            Admin admin = userDAO.validateAdmin(username, password);

            if (admin != null) {
                // Create session
                HttpSession session = request.getSession();
                session.setAttribute("userType", "admin");
                session.setAttribute("adminId", admin.getAdminId());
                session.setAttribute("username", admin.getUsername());
                session.setAttribute("fullName", admin.getFullName());
                session.setAttribute("email", admin.getEmail());

                // Redirect to admin dashboard
                response.sendRedirect("admin-dashboard.html");
            } else {
                // Invalid credentials
                showError(out, "Invalid admin credentials. Please try again.");
            }
        } else {
            // User login
            User user = userDAO.validateUser(username, password);

            if (user != null) {
                // Create session
                HttpSession session = request.getSession();
                session.setAttribute("userType", "user");
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("fullName", user.getFullName());
                session.setAttribute("email", user.getEmail());
                session.setAttribute("accountNumber", user.getAccountNumber());

                // Redirect to user dashboard
                response.sendRedirect("user-dashboard.html");
            } else {
                // Invalid credentials
                showError(out, "Invalid user credentials. Please try again.");
            }
        }
    }

    private void showError(PrintWriter out, String message) {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Login Error</title>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<div class='error-box'>");
        out.println("<h2>❌ Login Failed</h2>");
        out.println("<p>" + message + "</p>");
        out.println("<a href='login.html' class='btn'>← Back to Login</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
