# Servlet Concepts - Complete Guide

## Table of Contents
1. [Introduction to Servlets](#introduction-to-servlets)
2. [Web Application Architecture](#web-application-architecture)
3. [Servlet Lifecycle](#servlet-lifecycle)
4. [doGet vs doPost Methods](#doget-vs-dopost-methods)
5. [RequestDispatcher vs SendRedirect](#requestdispatcher-vs-sendredirect)
6. [Servlet-JDBC Integration](#servlet-jdbc-integration)

---

## Introduction to Servlets

### What is a Servlet?

A **Servlet** is a Java class that runs on a web server and handles HTTP requests and responses. It's part of the Java EE (Enterprise Edition) specification.

**Key Points:**
- Servlets are server-side components
- They extend the capabilities of web servers
- Platform-independent (runs on any server supporting Java)
- Used to create dynamic web content

### Why Use Servlets?

✅ **Platform Independent** - Write once, run anywhere  
✅ **Performance** - Faster than CGI (uses threads, not processes)  
✅ **Robust** - JVM managed memory, exception handling  
✅ **Secure** - Java security features built-in  
✅ **Extensible** - Can be extended easily

---

## Web Application Architecture

### Three-Tier Architecture

```
┌─────────────────────────────────────┐
│     Presentation Layer              │
│  (HTML, CSS, JavaScript)            │
│  - User Interface                   │
│  - Client-side validation           │
└──────────────┬──────────────────────┘
               │ HTTP Request/Response
               ↓
┌─────────────────────────────────────┐
│     Business Logic Layer            │
│  (Servlets)                         │
│  - Request processing               │
│  - Business rules                   │
│  - Data validation                  │
└──────────────┬──────────────────────┘
               │ JDBC
               ↓
┌─────────────────────────────────────┐
│     Data Access Layer               │
│  (DAO + Database)                   │
│  - CRUD operations                  │
│  - Data persistence                 │
└─────────────────────────────────────┘
```

### Request-Response Flow

```
1. Client (Browser) sends HTTP Request
   ↓
2. Web Server receives request
   ↓
3. Server creates HttpServletRequest and HttpServletResponse objects
   ↓
4. Server calls appropriate servlet's service() method
   ↓
5. service() routes to doGet(), doPost(), etc.
   ↓
6. Servlet processes request (may access database via JDBC)
   ↓
7. Servlet generates response (HTML, JSON, etc.)
   ↓
8. Server sends HttpServletResponse back to client
   ↓
9. Client displays the response
```

---

## Servlet Lifecycle

### The Three Main Methods

Every servlet goes through a lifecycle managed by the servlet container (e.g., Tomcat).

### 1. init() Method

**When Called:** Once, when the servlet is first loaded into memory

**Purpose:**
- Initialize resources (database connections, file handles, etc.)
- One-time setup operations
- Load configuration

**Signature:**
```java
public void init() throws ServletException {
    // Initialization code
    System.out.println("Servlet initialized!");
}
```

**Example from AccountServlet:**
```java
@Override
public void init() throws ServletException {
    super.init();
    initTime = new Date();
    System.out.println("[LIFECYCLE] init() called");
    System.out.println("[LIFECYCLE] AccountServlet initialized at: " + initTime);
}
```

**Need for init():**
- Expensive operations should be done once, not for every request
- Database connection pooling setup
- Loading configuration files
- Initializing shared resources

---

### 2. service() Method

**When Called:** For EVERY HTTP request to the servlet

**Purpose:**
- Determine request type (GET, POST, PUT, DELETE, etc.)
- Route request to appropriate method (doGet(), doPost(), etc.)
- Handle all HTTP methods

**Signature:**
```java
protected void service(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    // Request routing logic
}
```

**Example from AccountServlet:**
```java
@Override
protected void service(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    requestCount++;
    System.out.println("[LIFECYCLE] service() called");
    System.out.println("[LIFECYCLE] Request #" + requestCount);
    System.out.println("[LIFECYCLE] Method: " + request.getMethod());
    
    // Call parent service() which routes to doGet(), doPost(), etc.
    super.service(request, response);
}
```

**Need for service():**
- Central point for request handling
- Can implement custom routing logic
- Can add logging, authentication checks
- Usually don't override this - let it route to doGet()/doPost()

---

### 3. destroy() Method

**When Called:** Once, when the servlet is being unloaded from memory

**Purpose:**
- Cleanup resources
- Close database connections
- Save state if needed
- Final operations before shutdown

**Signature:**
```java
public void destroy() {
    // Cleanup code
    System.out.println("Servlet destroyed!");
}
```

**Example from AccountServlet:**
```java
@Override
public void destroy() {
    System.out.println("[LIFECYCLE] destroy() called");
    System.out.println("[LIFECYCLE] Total requests served: " + requestCount);
    System.out.println("[LIFECYCLE] Uptime: " + 
        (new Date().getTime() - initTime.getTime()) / 1000 + " seconds");
    super.destroy();
}
```

**Need for destroy():**
- Prevent resource leaks
- Close file handles, database connections
- Save important state
- Graceful shutdown

---

### Lifecycle Diagram

```
┌─────────────────────────────────────┐
│   Servlet Class Loaded              │
└──────────────┬──────────────────────┘
               │
               ↓
┌─────────────────────────────────────┐
│   init() - Called ONCE              │
│   - Initialize resources            │
└──────────────┬──────────────────────┘
               │
               ↓
┌─────────────────────────────────────┐
│   service() - Called for EACH       │
│   request                           │
│   ↓                                 │
│   Routes to:                        │
│   - doGet()                         │
│   - doPost()                        │
│   - doPut()                         │
│   - doDelete()                      │
└──────────────┬──────────────────────┘
               │ (Repeat for each request)
               │
               ↓
┌─────────────────────────────────────┐
│   destroy() - Called ONCE           │
│   - Cleanup resources               │
└─────────────────────────────────────┘
```

---

## doGet vs doPost Methods

### doGet() Method

**Purpose:** Handle HTTP GET requests

**Characteristics:**
- ✅ **Idempotent** - Can be called multiple times without side effects
- ✅ **Cacheable** - Responses can be cached by browser
- ✅ **Bookmarkable** - URL can be bookmarked
- ✅ **Parameters in URL** - Visible in address bar
- ⚠️ **Limited data** - URL length restrictions
- ⚠️ **Not secure** - Data visible in URL

**When to Use:**
- Retrieving/viewing data
- Search operations
- Filtering/sorting
- Navigation

**Example:**
```java
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
    // Get parameter from URL: ?accountNumber=12345
    String accNo = request.getParameter("accountNumber");
    
    // Retrieve data from database
    Account account = accountDAO.getAccount(Integer.parseInt(accNo));
    
    // Display data
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<h1>Account Details</h1>");
    out.println("<p>Balance: " + account.getBalance() + "</p>");
}
```

**URL Example:**
```
http://localhost:8080/BankingApp/ViewAccountServlet?accountNumber=12345
                                                     └─ Query Parameters
```

---

### doPost() Method

**Purpose:** Handle HTTP POST requests

**Characteristics:**
- ✅ **Non-idempotent** - May have side effects (create, update, delete)
- ✅ **Secure** - Data in request body, not URL
- ✅ **Large data** - No size restrictions
- ❌ **Not cacheable** - Responses not cached
- ❌ **Not bookmarkable** - Can't bookmark the request

**When to Use:**
- Creating new resources
- Updating data
- Deleting data
- Submitting forms with sensitive data

**Example:**
```java
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
    // Get parameters from request body (not URL)
    String name = request.getParameter("holderName");
    String balanceStr = request.getParameter("balance");
    
    // Create new account
    Account account = new Account();
    account.setHolderName(name);
    account.setBalance(Double.parseDouble(balanceStr));
    
    // Save to database
    accountDAO.createAccount(account);
    
    // Send response
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<h1>Account Created!</h1>");
}
```

**HTML Form:**
```html
<form action="CreateAccountServlet" method="POST">
    <input type="text" name="holderName">
    <input type="number" name="balance">
    <button type="submit">Create</button>
</form>
```

---

### Comparison Table

| Feature | doGet() | doPost() |
|---------|---------|----------|
| **Purpose** | Retrieve data | Submit data |
| **Data Location** | URL (query string) | Request body |
| **Visibility** | Visible in URL | Hidden |
| **Security** | Less secure | More secure |
| **Caching** | Can be cached | Not cached |
| **Bookmarking** | Can bookmark | Cannot bookmark |
| **Data Size** | Limited (~2KB) | Unlimited |
| **Idempotent** | Yes | No |
| **Use Cases** | Search, view, filter | Create, update, delete |

---

## RequestDispatcher vs SendRedirect

### RequestDispatcher (Server-Side Forward)

**What it does:** Forwards the request to another resource on the **same server**

**Characteristics:**
- ✅ **Server-side** - Happens on the server
- ✅ **URL doesn't change** - Browser URL remains the same
- ✅ **One request** - Single request-response cycle
- ✅ **Attributes preserved** - Can share data via request attributes
- ✅ **Fast** - No round trip to client
- ❌ **Same server only** - Can't forward to external URLs

**Syntax:**
```java
// Set attributes to pass data
request.setAttribute("message", "Transfer successful");
request.setAttribute("amount", 1000.0);

// Forward to another resource
RequestDispatcher dispatcher = request.getRequestDispatcher("confirmation.jsp");
dispatcher.forward(request, response);
```

**Example from TransferServlet:**
```java
// Process transfer
accountDAO.updateBalance(fromAccount, newFromBalance);
accountDAO.updateBalance(toAccount, newToBalance);

// Set attributes
request.setAttribute("fromAccount", fromAccount);
request.setAttribute("amount", amount);
request.setAttribute("success", true);

// Forward to confirmation page
RequestDispatcher dispatcher = request.getRequestDispatcher("transferConfirmation.jsp");
dispatcher.forward(request, response);
```

**Flow Diagram:**
```
Client → Servlet A → (forward) → Servlet B → Response → Client
         └─────────────────────────────────┘
                  One Request
         URL: /ServletA (doesn't change)
```

---

### SendRedirect (Client-Side Redirect)

**What it does:** Sends a redirect response to the client, which then makes a new request

**Characteristics:**
- ✅ **Client-side** - Browser makes new request
- ✅ **URL changes** - Browser URL updates
- ✅ **Can redirect anywhere** - External URLs allowed
- ❌ **Two requests** - Two request-response cycles
- ❌ **Attributes lost** - New request, attributes not preserved
- ❌ **Slower** - Round trip to client

**Syntax:**
```java
// Redirect to another page
response.sendRedirect("index.html");

// Can redirect to external URLs
response.sendRedirect("https://www.google.com");

// Can pass data via query parameters
response.sendRedirect("home.jsp?logout=success");
```

**Example from LogoutServlet:**
```java
// Clear session
HttpSession session = request.getSession(false);
if (session != null) {
    session.invalidate();
}

// Redirect to home page
response.sendRedirect("index.html?logout=success");
```

**Flow Diagram:**
```
Client → Servlet A → (302 redirect) → Client → Servlet B → Response → Client
         └──────────────────────────┘         └─────────────────────┘
               First Request                      Second Request
         URL: /ServletA                       URL: /ServletB (changes!)
```

---

### Comparison Table

| Feature | RequestDispatcher | SendRedirect |
|---------|-------------------|--------------|
| **Location** | Server-side | Client-side |
| **URL Change** | No | Yes |
| **Number of Requests** | One | Two |
| **Speed** | Faster | Slower |
| **Request Attributes** | Preserved | Lost |
| **Scope** | Same server only | Any URL |
| **Use Case** | Forward to JSP/servlet | Redirect after POST, logout |
| **Method** | `dispatcher.forward()` | `response.sendRedirect()` |

---

### When to Use Which?

**Use RequestDispatcher when:**
- Forwarding to JSP for view rendering
- Passing data to another servlet
- Want to hide the actual resource URL
- Need to preserve request attributes
- Performance is critical

**Use SendRedirect when:**
- Redirecting after form submission (POST-Redirect-GET pattern)
- Logout functionality
- Redirecting to external websites
- Want the URL to change in browser
- Preventing form resubmission on refresh

---

## Servlet-JDBC Integration

### Architecture

```
┌─────────────────────────────────────┐
│   Servlet Layer                     │
│   - CreateAccountServlet            │
│   - DepositServlet                  │
│   - WithdrawServlet                 │
└──────────────┬──────────────────────┘
               │ Uses
               ↓
┌─────────────────────────────────────┐
│   DAO Layer                         │
│   - AccountDAO                      │
│   - CRUD operations                 │
└──────────────┬──────────────────────┘
               │ Uses
               ↓
┌─────────────────────────────────────┐
│   Database Utility                  │
│   - DatabaseUtil                    │
│   - Connection management           │
└──────────────┬──────────────────────┘
               │ JDBC
               ↓
┌─────────────────────────────────────┐
│   MySQL Database                    │
│   - accounts table                  │
└─────────────────────────────────────┘
```

### Complete Example

**1. DatabaseUtil.java** - Connection Management
```java
public class DatabaseUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bankdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root123";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
```

**2. AccountDAO.java** - Data Access Object
```java
public class AccountDAO {
    public boolean createAccount(Account account) {
        String sql = "INSERT INTO accounts (account_number, holder_name, balance, email, account_type) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, account.getAccountNumber());
            pstmt.setString(2, account.getHolderName());
            pstmt.setDouble(3, account.getBalance());
            pstmt.setString(4, account.getEmail());
            pstmt.setString(5, account.getAccountType());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
```

**3. CreateAccountServlet.java** - Servlet
```java
public class CreateAccountServlet extends HttpServlet {
    private AccountDAO accountDAO;
    
    @Override
    public void init() {
        accountDAO = new AccountDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get form data
        int accNo = Integer.parseInt(request.getParameter("accountNumber"));
        String name = request.getParameter("holderName");
        double balance = Double.parseDouble(request.getParameter("balance"));
        
        // Create account object
        Account account = new Account(accNo, name, balance, email, type);
        
        // Save to database via DAO
        boolean success = accountDAO.createAccount(account);
        
        // Send response
        if (success) {
            response.getWriter().println("Account created successfully!");
        } else {
            response.getWriter().println("Failed to create account");
        }
    }
}
```

### Best Practices

1. **Use DAO Pattern** - Separate database logic from servlet logic
2. **PreparedStatements** - Prevent SQL injection
3. **Try-with-resources** - Automatic resource cleanup
4. **Connection Pooling** - Reuse connections for better performance
5. **Transaction Management** - Use transactions for multiple operations
6. **Error Handling** - Proper exception handling and logging

---

## Summary

### Key Takeaways

1. **Servlets** are Java classes that handle HTTP requests on the server
2. **Lifecycle** has three main methods: init(), service(), destroy()
3. **doGet()** for retrieving data, **doPost()** for submitting data
4. **RequestDispatcher** for server-side forwarding (URL doesn't change)
5. **SendRedirect** for client-side redirection (URL changes)
6. **JDBC Integration** connects servlets to databases via DAO pattern

### Learning Path

1. ✅ Understand servlet basics and lifecycle
2. ✅ Learn doGet() and doPost() differences
3. ✅ Master RequestDispatcher vs SendRedirect
4. ✅ Integrate JDBC for database operations
5. ✅ Build complete web applications

---

**🎓 You're now ready to build servlet-based web applications!**
