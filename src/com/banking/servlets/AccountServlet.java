package com.banking.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.Date;

/**
 * AccountServlet - Demonstrates Servlet Lifecycle Methods
 * 
 * This servlet demonstrates the three main lifecycle methods:
 * 1. init() - Called once when servlet is first loaded
 * 2. service() - Called for each HTTP request
 * 3. destroy() - Called when servlet is being unloaded
 */
public class AccountServlet extends HttpServlet {

    private int requestCount = 0;
    private Date initTime;

    /**
     * init() - Initialization Method
     * Called ONCE when the servlet is first loaded into memory
     * Used for one-time initialization tasks
     */
    @Override
    public void init() throws ServletException {
        super.init();
        initTime = new Date();
        System.out.println("===========================================");
        System.out.println("[LIFECYCLE] init() called");
        System.out.println("[LIFECYCLE] AccountServlet initialized at: " + initTime);
        System.out.println("[LIFECYCLE] This method is called ONLY ONCE");
        System.out.println("===========================================");
    }

    /**
     * service() - Request Handling Method
     * Called for EVERY request to this servlet
     * Routes requests to doGet(), doPost(), etc.
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        requestCount++;
        System.out.println("-------------------------------------------");
        System.out.println("[LIFECYCLE] service() called");
        System.out.println("[LIFECYCLE] Request #" + requestCount);
        System.out.println("[LIFECYCLE] Method: " + request.getMethod());
        System.out.println("[LIFECYCLE] This method is called for EVERY request");
        System.out.println("-------------------------------------------");

        // Call parent service() which routes to doGet(), doPost(), etc.
        super.service(request, response);
    }

    /**
     * doGet() - Handle GET requests
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet Lifecycle Demo</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }");
        out.println(
                ".container { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        out.println("h1 { color: #2c3e50; }");
        out.println(".info { background: #e8f4f8; padding: 15px; border-left: 4px solid #3498db; margin: 10px 0; }");
        out.println(
                ".lifecycle { background: #fff3cd; padding: 15px; border-left: 4px solid #ffc107; margin: 10px 0; }");
        out.println("code { background: #f4f4f4; padding: 2px 6px; border-radius: 3px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>🔄 Servlet Lifecycle Demonstration</h1>");

        out.println("<div class='lifecycle'>");
        out.println("<h2>Lifecycle Information</h2>");
        out.println("<p><strong>Servlet initialized at:</strong> " + initTime + "</p>");
        out.println("<p><strong>Total requests served:</strong> " + requestCount + "</p>");
        out.println("<p><strong>Current time:</strong> " + new Date() + "</p>");
        out.println("</div>");

        out.println("<div class='info'>");
        out.println("<h2>📚 Servlet Lifecycle Methods</h2>");
        out.println("<h3>1. init()</h3>");
        out.println("<p><strong>When called:</strong> Once, when servlet is first loaded</p>");
        out.println("<p><strong>Purpose:</strong> Initialize resources, database connections, etc.</p>");
        out.println("<p><strong>Check console:</strong> You'll see init() was called only once!</p>");
        out.println("</div>");

        out.println("<div class='info'>");
        out.println("<h3>2. service()</h3>");
        out.println("<p><strong>When called:</strong> For every HTTP request</p>");
        out.println("<p><strong>Purpose:</strong> Route requests to doGet(), doPost(), etc.</p>");
        out.println("<p><strong>Check console:</strong> service() is called for each request (Request #" + requestCount
                + ")</p>");
        out.println("</div>");

        out.println("<div class='info'>");
        out.println("<h3>3. destroy()</h3>");
        out.println("<p><strong>When called:</strong> Once, when servlet is being unloaded</p>");
        out.println("<p><strong>Purpose:</strong> Cleanup resources, close connections, etc.</p>");
        out.println("<p><strong>To see this:</strong> Stop the Tomcat server and check console!</p>");
        out.println("</div>");

        out.println("<div class='lifecycle'>");
        out.println("<h2>🔍 Try This</h2>");
        out.println("<p>1. Refresh this page multiple times</p>");
        out.println("<p>2. Watch the request count increase</p>");
        out.println("<p>3. Check the console logs to see service() being called</p>");
        out.println("<p>4. Notice that init() was called only once!</p>");
        out.println("<p><button onclick='location.reload()'>🔄 Refresh Page</button></p>");
        out.println("</div>");

        out.println("<p><a href='/BankingApp/'>← Back to Home</a></p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * destroy() - Cleanup Method
     * Called ONCE when servlet is being unloaded from memory
     * Used for cleanup tasks
     */
    @Override
    public void destroy() {
        System.out.println("===========================================");
        System.out.println("[LIFECYCLE] destroy() called");
        System.out.println("[LIFECYCLE] AccountServlet is being destroyed");
        System.out.println("[LIFECYCLE] Total requests served: " + requestCount);
        System.out.println("[LIFECYCLE] Uptime: " + (new Date().getTime() - initTime.getTime()) / 1000 + " seconds");
        System.out.println("[LIFECYCLE] This method is called ONLY ONCE");
        System.out.println("===========================================");
        super.destroy();
    }
}
