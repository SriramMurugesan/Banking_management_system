import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/transaction")
public class TransactionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int accNo = Integer.parseInt(req.getParameter("accountNumber"));
            double amount = Double.parseDouble(req.getParameter("amount"));
            String type = req.getParameter("type"); // Deposit or Withdraw

            AccountDAO.updateBalance(accNo, amount, type);

            // Use RequestDispatcher to forward to success page (demonstrating dispatch)
            RequestDispatcher dispatcher = req.getRequestDispatcher("success.html");
            dispatcher.forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            resp.setContentType("text/html");
            resp.getWriter().write("Transaction Failed: " + e.getMessage());
            resp.getWriter().write("<br><a href='transaction.html'>Try Again</a>");
        } catch (NumberFormatException e) {
            resp.getWriter().write("Invalid input!");
        }
    }
}
