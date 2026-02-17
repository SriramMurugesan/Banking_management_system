# Banking Web Application - Servlet Edition

A complete servlet-based web application demonstrating all key servlet concepts with JDBC integration.

## 🎯 Learning Objectives

This application demonstrates:

- ✅ **Servlet Lifecycle** - init(), service(), destroy()
- ✅ **HTTP Methods** - doGet() vs doPost()
- ✅ **Request Forwarding** - RequestDispatcher
- ✅ **Response Redirection** - SendRedirect
- ✅ **Database Integration** - Servlet-JDBC with DAO pattern
- ✅ **Web Architecture** - Three-tier architecture

## 📁 Project Structure

```
Banking_management_system/
├── src/
│   └── com/banking/
│       ├── model/          # Data models
│       ├── dao/            # Database access layer
│       └── servlets/       # Servlet controllers
├── webapp/
│   ├── *.html             # Frontend pages
│   ├── css/               # Stylesheets
│   └── WEB-INF/
│       ├── web.xml        # Deployment descriptor
│       ├── classes/       # Compiled servlets
│       └── lib/           # JDBC driver
└── docs/                  # Documentation
```

## 🚀 Quick Start

### Prerequisites

- JDK 8+
- Apache Tomcat 9.0+
- MySQL Server
- MySQL JDBC Driver

### Compilation

```bash
# Compile servlets
javac -cp "tomcat/lib/servlet-api.jar:webapp/WEB-INF/lib/mysql-connector-j-8.0.33.jar" \
      -d webapp/WEB-INF/classes \
      src/com/banking/*/*.java
```

### Deployment

```bash
# Copy to Tomcat webapps
cp -r webapp /path/to/tomcat/webapps/BankingApp

# Start Tomcat
/path/to/tomcat/bin/startup.sh
```

### Access

Open browser: `http://localhost:8080/BankingApp/`

## 📚 Documentation

- **[SERVLET_CONCEPTS.md](SERVLET_CONCEPTS.md)** - Complete servlet concepts guide
- **[DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md)** - Step-by-step deployment instructions
- **[WINDOWS_SETUP_GUIDE.md](WINDOWS_SETUP_GUIDE.md)** - Windows-specific MySQL setup
- **[JDBC_README.md](JDBC_README.md)** - JDBC integration details

## 🎓 Servlet Concepts Covered

### 1. Servlet Lifecycle

**File:** `AccountServlet.java`

Demonstrates:
- `init()` - Called once on servlet load
- `service()` - Called for every request
- `destroy()` - Called on servlet unload

**Try it:** Visit `/AccountServlet` and check console logs

### 2. doGet() Method

**File:** `ViewAccountServlet.java`

Demonstrates:
- Handling GET requests
- Query parameters in URL
- Idempotent operations

**Try it:** Search for an account - notice the URL changes

### 3. doPost() Method

**Files:** `CreateAccountServlet.java`, `DepositServlet.java`, `WithdrawServlet.java`

Demonstrates:
- Handling POST requests
- Form data in request body
- Non-idempotent operations

**Try it:** Create an account - data not visible in URL

### 4. RequestDispatcher

**File:** `TransferServlet.java`

Demonstrates:
- Server-side forwarding
- URL doesn't change
- Request attribute sharing

**Try it:** Transfer funds - URL stays `/TransferServlet`

### 5. SendRedirect

**File:** `LogoutServlet.java`

Demonstrates:
- Client-side redirection
- URL changes
- New request created

**Try it:** Logout - URL changes to `/index.html`

### 6. Servlet-JDBC Integration

**Files:** `DatabaseUtil.java`, `AccountDAO.java`

Demonstrates:
- Database connection management
- DAO pattern
- CRUD operations with PreparedStatements

**Try it:** All operations persist to MySQL database

## 🔧 Features

### Banking Operations

- ➕ **Create Account** - Open savings or current account
- 👁️ **View Account** - Check account details and balance
- 💰 **Deposit** - Add funds to account
- 💸 **Withdraw** - Withdraw funds with validation
- 🔄 **Transfer** - Transfer between accounts

### Technical Features

- Automatic database creation
- SQL injection prevention (PreparedStatements)
- Input validation
- Error handling
- Modern responsive UI

## 🗄️ Database Schema

```sql
CREATE TABLE accounts (
    account_number INT PRIMARY KEY,
    holder_name VARCHAR(100),
    balance DOUBLE,
    email VARCHAR(100),
    account_type VARCHAR(20)
);
```

## 🎨 Screenshots

### Home Page
Modern card-based navigation with servlet concepts explanation

### Lifecycle Demo
Real-time servlet lifecycle method logging

### Operations
Clean forms with inline servlet concept explanations

## 📖 Code Examples

### Creating an Account (doPost)

```java
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    String name = request.getParameter("holderName");
    double balance = Double.parseDouble(request.getParameter("balance"));
    
    Account account = new Account(accNo, name, balance, email, type);
    accountDAO.createAccount(account);
    
    response.getWriter().println("Account created!");
}
```

### Viewing Account (doGet)

```java
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    int accNo = Integer.parseInt(request.getParameter("accountNumber"));
    Account account = accountDAO.getAccount(accNo);
    
    // Display account details
}
```

### Transfer with RequestDispatcher

```java
// Process transfer
accountDAO.updateBalance(fromAccount, newBalance);

// Forward to confirmation
request.setAttribute("amount", amount);
RequestDispatcher dispatcher = request.getRequestDispatcher("confirmation.jsp");
dispatcher.forward(request, response);
```

## 🧪 Testing

### Manual Testing

1. Start MySQL server
2. Deploy application to Tomcat
3. Access `http://localhost:8080/BankingApp/`
4. Test each operation
5. Check console logs for lifecycle methods

### Verification Checklist

- [ ] Database created automatically
- [ ] Servlet lifecycle logs appear
- [ ] Create account works (doPost)
- [ ] View account works (doGet)
- [ ] Deposit/withdraw update database
- [ ] Transfer uses RequestDispatcher
- [ ] Logout uses SendRedirect

## 🛠️ Troubleshooting

### Common Issues

**404 Not Found**
- Check application deployed to `webapps/BankingApp`
- Verify `web.xml` servlet mappings

**ClassNotFoundException**
- Ensure servlets compiled to `WEB-INF/classes`
- Check package structure matches directory

**SQL Exception**
- Verify MySQL is running
- Check database credentials in `DatabaseUtil.java`
- Ensure JDBC driver in `WEB-INF/lib`

See [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) for detailed troubleshooting.

## 📝 Assignment Ideas

1. Add transaction history feature
2. Implement user authentication
3. Add interest calculation servlet
4. Create admin panel
5. Add session management
6. Implement filters for logging

## 🤝 Contributing

This is an educational project. Feel free to:
- Add more servlet examples
- Improve documentation
- Add new features
- Fix bugs

## 📄 License

Educational use only.

## 👨‍🏫 For Instructors

This project is designed for teaching servlet concepts:

- Each servlet demonstrates a specific concept
- Code includes extensive comments
- Console logging shows lifecycle
- HTML pages explain concepts inline
- Complete documentation provided

Perfect for:
- Java EE courses
- Web development classes
- Servlet workshops
- JDBC integration labs

---

**Built with ❤️ for learning Java Servlets**
