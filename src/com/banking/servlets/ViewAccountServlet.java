package com.banking.servlets;

import com.banking.dao.AccountDAO;
import com.banking.model.Account;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 * ViewAccountServlet - Demonstrates doGet() method
 * Handles GET requests to view account details
 */
public class ViewAccountServlet extends HttpServlet {

    private AccountDAO accountDAO;

    @Override
    public void init() {
        accountDAO = new AccountDAO();
        System.out.println("[ViewAccountServlet] Initialized");
    }

    /**
     * doGet() - Handles GET requests
     * GET is used for retrieving/viewing data
     * Parameters are visible in URL (query string)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("[ViewAccountServlet] doGet() called - Retrieving account details");

        String accNoStr = request.getParameter("accountNumber");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>View Account</title>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");

        if (accNoStr == null || accNoStr.trim().isEmpty()) {
            // Show search form
            out.println("<h1>🔍 View Account Details</h1>");
            out.println("<form method='GET' action='ViewAccountServlet'>");
            out.println("<div class='form-group'>");
            out.println("<label>Account Number:</label>");
            out.println("<input type='number' name='accountNumber' required>");
            out.println("</div>");
            out.println("<button type='submit' class='btn'>Search Account</button>");
            out.println("</form>");
            out.println("<div class='info-box'>");
            out.println("<h3>💡 GET Method</h3>");
            out.println("<p>This form uses <code>GET</code> method.</p>");
            out.println("<p>When you submit, the account number will appear in the URL.</p>");
            out.println("<p>Example: <code>ViewAccountServlet?accountNumber=12345</code></p>");
            out.println("<p><strong>Use GET for:</strong> Retrieving/viewing data</p>");
            out.println("</div>");
        } else {
            try {
                int accountNumber = Integer.parseInt(accNoStr);
                Account account = accountDAO.getAccount(accountNumber);

                if (account != null) {
                    out.println("<div class='success-box'>");
                    out.println("<h1>📊 Account Details</h1>");
                    out.println("<div class='account-details'>");
                    out.println("<p><strong>Account Number:</strong> " + account.getAccountNumber() + "</p>");
                    out.println("<p><strong>Account Holder:</strong> " + account.getHolderName() + "</p>");
                    out.println("<p><strong>Account Type:</strong> " + account.getAccountType() + "</p>");
                    out.println("<p><strong>Current Balance:</strong> ₹" + account.getBalance() + "</p>");
                    out.println("<p><strong>Email:</strong> " + account.getEmail() + "</p>");
                    out.println("</div>");
                    out.println("<div class='info-box'>");
                    out.println("<h3>💡 What just happened?</h3>");
                    out.println("<p>The <code>doGet()</code> method retrieved this account from the database.</p>");
                    out.println("<p>Notice the URL contains: <code>?accountNumber=" + accountNumber + "</code></p>");
                    out.println("<p>GET requests are idempotent - you can refresh without side effects!</p>");
                    out.println("</div>");
                    out.println("</div>");

                    System.out.println("[ViewAccountServlet] Account found: " + accountNumber);
                } else {
                    out.println("<div class='error-box'>");
                    out.println("<h1>❌ Account Not Found</h1>");
                    out.println("<p>No account found with number: " + accountNumber + "</p>");
                    out.println("</div>");
                }
            } catch (NumberFormatException e) {
                out.println("<div class='error-box'>");
                out.println("<h1>❌ Invalid Input</h1>");
                out.println("<p>Please enter a valid account number.</p>");
                out.println("</div>");
            }
        }

        out.println("<p><a href='viewAccount.html' class='btn'>← Search Another Account</a></p>");
        out.println("<p><a href='index.html' class='btn'>Home</a></p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * doPost() - Not typically used for viewing data
     * Redirect to doGet
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
