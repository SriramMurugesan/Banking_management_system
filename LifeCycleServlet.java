import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/lifecycle")
public class LifeCycleServlet extends HttpServlet {

    public LifeCycleServlet() {
        System.out.println("LifeCycleServlet: Constructor called");
    }

    @Override
    public void init() throws ServletException {
        System.out.println("LifeCycleServlet: init() method called");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LifeCycleServlet: service() method called");
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LifeCycleServlet: doGet() method called");
        resp.getWriter().write("Check the server console for lifecycle logs!");
    }

    @Override
    public void destroy() {
        System.out.println("LifeCycleServlet: destroy() method called");
    }
}
