package com.banking.servlets;

import com.banking.dao.AccountDAO;
import com.banking.model.Account;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 * WithdrawServlet - Handles withdrawal operations
 */
public class WithdrawServlet extends HttpServlet {

    private AccountDAO accountDAO;

    @Override
    public void init() {
        accountDAO = new AccountDAO();
        System.out.println("[WithdrawServlet] Initialized");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("[WithdrawServlet] Processing withdrawal");

        String accNoStr = request.getParameter("accountNumber");
        String amountStr = request.getParameter("amount");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            int accountNumber = Integer.parseInt(accNoStr);
            double amount = Double.parseDouble(amountStr);

            if (amount <= 0) {
                throw new Exception("Withdrawal amount must be positive");
            }

            Account account = accountDAO.getAccount(accountNumber);
            if (account == null) {
                throw new Exception("Account " + accountNumber + " not found");
            }

            if (account.getBalance() < amount) {
                throw new Exception("Insufficient balance. Available: ₹" + account.getBalance());
            }

            double newBalance = account.getBalance() - amount;
            accountDAO.updateBalance(accountNumber, newBalance);

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Withdrawal Success</title>");
            out.println("<link rel='stylesheet' href='css/style.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<div class='success-box'>");
            out.println("<h1>✅ Withdrawal Successful!</h1>");
            out.println("<div class='account-details'>");
            out.println("<p><strong>Account Number:</strong> " + accountNumber + "</p>");
            out.println("<p><strong>Account Holder:</strong> " + account.getHolderName() + "</p>");
            out.println("<p><strong>Amount Withdrawn:</strong> ₹" + amount + "</p>");
            out.println("<p><strong>Previous Balance:</strong> ₹" + account.getBalance() + "</p>");
            out.println("<p><strong>New Balance:</strong> ₹" + newBalance + "</p>");
            out.println("</div>");
            out.println("</div>");
            out.println("<p><a href='withdraw.html' class='btn'>Make Another Withdrawal</a></p>");
            out.println("<p><a href='index.html' class='btn'>Home</a></p>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

            System.out
                    .println("[WithdrawServlet] Withdrawal successful: ₹" + amount + " from account " + accountNumber);

        } catch (Exception e) {
            showError(out, e.getMessage());
        }
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
        out.println("<p><a href='withdraw.html' class='btn'>← Try Again</a></p>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
