import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/createAccount", "/viewDetails" })
public class AccountServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Handle Account Creation
        String action = req.getServletPath();

        if ("/createAccount".equals(action)) {
            try {
                int accNo = Integer.parseInt(req.getParameter("accountNumber"));
                String name = req.getParameter("holderName");
                double balance = Double.parseDouble(req.getParameter("balance"));
                String email = req.getParameter("email");
                String type = req.getParameter("type");

                AccountDAO.createAccount(accNo, name, balance, email, type);

                // Redirect to success page
                resp.sendRedirect("success.html");

            } catch (SQLException e) {
                e.printStackTrace();
                resp.getWriter().write("Error creating account: " + e.getMessage());
            } catch (NumberFormatException e) {
                resp.getWriter().write("Invalid number format!");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Handle View Details
        String action = req.getServletPath();

        if ("/viewDetails".equals(action)) {
            try {
                int accNo = Integer.parseInt(req.getParameter("accountNumber"));
                String details = AccountDAO.getAccountDetails(accNo);

                resp.setContentType("text/html");
                PrintWriter out = resp.getWriter();
                out.println("<html><body>");
                out.println("<h2>Account Details</h2>");
                out.println(details);
                out.println("<br><br><a href='index.html'>Back to Home</a>");
                out.println("</body></html>");

            } catch (SQLException e) {
                e.printStackTrace();
                resp.getWriter().write("Error retrieving account: " + e.getMessage());
            } catch (NumberFormatException e) {
                resp.getWriter().write("Invalid Account Number!");
            }
        }
    }
}
