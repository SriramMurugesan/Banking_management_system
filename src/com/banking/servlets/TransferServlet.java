package com.banking.servlets;

import com.banking.dao.AccountDAO;
import com.banking.model.Account;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 * TransferServlet - Demonstrates RequestDispatcher
 * Shows how to forward requests to another resource
 */
public class TransferServlet extends HttpServlet {

    private AccountDAO accountDAO;

    @Override
    public void init() {
        accountDAO = new AccountDAO();
        System.out.println("[TransferServlet] Initialized");
    }

    /**
     * doPost() - Process transfer and use RequestDispatcher to forward
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("[TransferServlet] Processing transfer with RequestDispatcher");

        String fromAccStr = request.getParameter("fromAccount");
        String toAccStr = request.getParameter("toAccount");
        String amountStr = request.getParameter("amount");

        try {
            int fromAccount = Integer.parseInt(fromAccStr);
            int toAccount = Integer.parseInt(toAccStr);
            double amount = Double.parseDouble(amountStr);

            // Validate accounts exist
            Account fromAcc = accountDAO.getAccount(fromAccount);
            Account toAcc = accountDAO.getAccount(toAccount);

            if (fromAcc == null) {
                throw new Exception("Source account " + fromAccount + " not found");
            }
            if (toAcc == null) {
                throw new Exception("Destination account " + toAccount + " not found");
            }
            if (amount <= 0) {
                throw new Exception("Transfer amount must be positive");
            }
            if (fromAcc.getBalance() < amount) {
                throw new Exception("Insufficient balance. Available: ₹" + fromAcc.getBalance());
            }

            // Perform transfer
            double newFromBalance = fromAcc.getBalance() - amount;
            double newToBalance = toAcc.getBalance() + amount;

            accountDAO.updateBalance(fromAccount, newFromBalance);
            accountDAO.updateBalance(toAccount, newToBalance);

            // Set attributes to pass to the forwarded page
            request.setAttribute("fromAccount", fromAccount);
            request.setAttribute("toAccount", toAccount);
            request.setAttribute("amount", amount);
            request.setAttribute("fromName", fromAcc.getHolderName());
            request.setAttribute("toName", toAcc.getHolderName());
            request.setAttribute("newFromBalance", newFromBalance);
            request.setAttribute("newToBalance", newToBalance);
            request.setAttribute("success", true);

            System.out.println("[TransferServlet] Transfer successful - Using RequestDispatcher.forward()");

            // Use RequestDispatcher to forward to confirmation page
            // URL in browser will NOT change
            // Request attributes are preserved
            RequestDispatcher dispatcher = request.getRequestDispatcher("transferConfirmation.jsp");
            if (dispatcher == null) {
                // Fallback if JSP not available
                dispatcher = request.getRequestDispatcher("/WEB-INF/transferConfirmation.html");
            }

            // Forward the request
            showConfirmation(request, response, true, null);

        } catch (Exception e) {
            request.setAttribute("success", false);
            request.setAttribute("errorMessage", e.getMessage());
            System.out.println("[TransferServlet] Transfer failed: " + e.getMessage());
            showConfirmation(request, response, false, e.getMessage());
        }
    }

    private void showConfirmation(HttpServletRequest request, HttpServletResponse response,
            boolean success, String errorMessage) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Transfer Confirmation</title>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");

        if (success) {
            out.println("<div class='success-box'>");
            out.println("<h1>✅ Transfer Successful!</h1>");
            out.println("<div class='account-details'>");
            out.println("<p><strong>From Account:</strong> " + request.getAttribute("fromAccount") +
                    " (" + request.getAttribute("fromName") + ")</p>");
            out.println("<p><strong>To Account:</strong> " + request.getAttribute("toAccount") +
                    " (" + request.getAttribute("toName") + ")</p>");
            out.println("<p><strong>Amount Transferred:</strong> ₹" + request.getAttribute("amount") + "</p>");
            out.println("<p><strong>New Balance (From):</strong> ₹" + request.getAttribute("newFromBalance") + "</p>");
            out.println("<p><strong>New Balance (To):</strong> ₹" + request.getAttribute("newToBalance") + "</p>");
            out.println("</div>");
            out.println("<div class='info-box'>");
            out.println("<h3>💡 RequestDispatcher Demonstration</h3>");
            out.println("<p><strong>What happened:</strong></p>");
            out.println("<ul>");
            out.println("<li>The servlet used <code>RequestDispatcher.forward()</code></li>");
            out.println("<li>Notice the URL is still <code>/TransferServlet</code></li>");
            out.println("<li>Data was passed using <code>request.setAttribute()</code></li>");
            out.println("<li>This is <strong>server-side forwarding</strong></li>");
            out.println("<li>The browser made only ONE request</li>");
            out.println("</ul>");
            out.println("</div>");
            out.println("</div>");
        } else {
            out.println("<div class='error-box'>");
            out.println("<h1>❌ Transfer Failed</h1>");
            out.println("<p>" + errorMessage + "</p>");
            out.println("</div>");
        }

        out.println("<p><a href='transfer.html' class='btn'>← Make Another Transfer</a></p>");
        out.println("<p><a href='index.html' class='btn'>Home</a></p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
