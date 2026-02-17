package com.banking.servlets;

import com.banking.dao.AccountDAO;
import com.banking.model.Account;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 * CreateAccountServlet - Demonstrates doPost() method
 * Handles POST requests from the account creation form
 */
public class CreateAccountServlet extends HttpServlet {

    private AccountDAO accountDAO;

    @Override
    public void init() {
        accountDAO = new AccountDAO();
        System.out.println("[CreateAccountServlet] Initialized");
    }

    /**
     * doPost() - Handles POST requests
     * POST is used for creating/submitting data
     * Data is sent in request body (not visible in URL)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("[CreateAccountServlet] doPost() called - Processing account creation");

        // Get form parameters
        String accNoStr = request.getParameter("accountNumber");
        String holderName = request.getParameter("holderName");
        String balanceStr = request.getParameter("balance");
        String email = request.getParameter("email");
        String accountType = request.getParameter("accountType");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Validate input
            if (accNoStr == null || holderName == null || balanceStr == null ||
                    email == null || accountType == null) {
                throw new Exception("All fields are required");
            }

            int accountNumber = Integer.parseInt(accNoStr);
            double balance = Double.parseDouble(balanceStr);

            if (balance < 0) {
                throw new Exception("Balance cannot be negative");
            }

            // Check if account already exists
            if (accountDAO.accountExists(accountNumber)) {
                throw new Exception("Account number " + accountNumber + " already exists");
            }

            // Create account object
            Account account = new Account(accountNumber, holderName, balance, email, accountType);

            // Save to database
            boolean success = accountDAO.createAccount(account);

            if (success) {
                // Success response
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Account Created</title>");
                out.println("<link rel='stylesheet' href='css/style.css'>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class='container'>");
                out.println("<div class='success-box'>");
                out.println("<h1>✅ Account Created Successfully!</h1>");
                out.println("<div class='account-details'>");
                out.println("<p><strong>Account Number:</strong> " + accountNumber + "</p>");
                out.println("<p><strong>Account Holder:</strong> " + holderName + "</p>");
                out.println("<p><strong>Account Type:</strong> " + accountType + "</p>");
                out.println("<p><strong>Initial Balance:</strong> ₹" + balance + "</p>");
                out.println("<p><strong>Email:</strong> " + email + "</p>");
                out.println("</div>");
                out.println("<div class='info-box'>");
                out.println("<h3>💡 What just happened?</h3>");
                out.println("<p>This form used the <code>POST</code> method to submit data.</p>");
                out.println("<p>The <code>doPost()</code> method in the servlet processed your request.</p>");
                out.println("<p>Data was sent securely in the request body (not in URL).</p>");
                out.println("</div>");
                out.println("<p><a href='index.html' class='btn'>← Back to Home</a></p>");
                out.println("<p><a href='viewAccount.html' class='btn'>View Account Details</a></p>");
                out.println("</div>");
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");

                System.out.println("[CreateAccountServlet] Account created successfully: " + accountNumber);
            } else {
                throw new Exception("Failed to create account in database");
            }

        } catch (NumberFormatException e) {
            showError(out, "Invalid number format. Please enter valid numbers.");
        } catch (Exception e) {
            showError(out, e.getMessage());
        }
    }

    /**
     * doGet() - Redirect to creation form
     * GET is typically used for retrieving/displaying data
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect to the account creation form
        response.sendRedirect("createAccount.html");
    }

    private void showError(PrintWriter out, String message) {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Error</title>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<div class='error-box'>");
        out.println("<h1>❌ Error</h1>");
        out.println("<p>" + message + "</p>");
        out.println("<p><a href='createAccount.html' class='btn'>← Try Again</a></p>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
