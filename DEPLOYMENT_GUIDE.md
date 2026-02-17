# Servlet Banking Application - Deployment Guide

## Prerequisites

Before deploying the application, ensure you have:

1. ✅ **JDK 8 or higher** installed
2. ✅ **Apache Tomcat 9.0+** installed
3. ✅ **MySQL Server** running
4. ✅ **MySQL JDBC Driver** (mysql-connector-j-8.0.33.jar)

---

## Step 1: Install Apache Tomcat

### Windows

1. Download Tomcat from: https://tomcat.apache.org/download-90.cgi
2. Choose **"32-bit/64-bit Windows Service Installer"**
3. Run the installer
4. Set installation directory (e.g., `C:\Program Files\Apache Software Foundation\Tomcat 9.0`)
5. Set admin username and password
6. Complete installation

### Linux

```bash
# Download Tomcat
wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.80/bin/apache-tomcat-9.0.80.tar.gz

# Extract
tar -xzf apache-tomcat-9.0.80.tar.gz

# Move to /opt
sudo mv apache-tomcat-9.0.80 /opt/tomcat

# Set permissions
sudo chmod +x /opt/tomcat/bin/*.sh
```

---

## Step 2: Compile the Servlet Application

### Compile Java Files

```bash
# Navigate to project directory
cd /path/to/Banking_management_system

# Create classes directory
mkdir -p webapp/WEB-INF/classes

# Compile with servlet-api.jar from Tomcat
javac -cp "/path/to/tomcat/lib/servlet-api.jar:webapp/WEB-INF/lib/mysql-connector-j-8.0.33.jar" \
      -d webapp/WEB-INF/classes \
      src/com/banking/model/*.java \
      src/com/banking/dao/*.java \
      src/com/banking/servlets/*.java
```

### Windows Compilation

```cmd
REM Navigate to project
cd C:\path\to\Banking_management_system

REM Create classes directory
mkdir webapp\WEB-INF\classes

REM Compile (adjust Tomcat path)
javac -cp "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\servlet-api.jar;webapp\WEB-INF\lib\mysql-connector-j-8.0.33.jar" -d webapp\WEB-INF\classes src\com\banking\model\*.java src\com\banking\dao\*.java src\com\banking\servlets\*.java
```

---

## Step 3: Configure Database

### Update Database Credentials

Edit `src/com/banking/dao/DatabaseUtil.java`:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/bankdb";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "your_password_here";  // ← Change this
```

### Recompile After Changes

```bash
# Recompile DatabaseUtil
javac -cp "/path/to/tomcat/lib/servlet-api.jar:webapp/WEB-INF/lib/mysql-connector-j-8.0.33.jar" \
      -d webapp/WEB-INF/classes \
      src/com/banking/dao/DatabaseUtil.java
```

---

## Step 4: Deploy to Tomcat

### Method 1: Copy to webapps (Recommended)

```bash
# Copy entire webapp folder to Tomcat webapps
cp -r webapp /path/to/tomcat/webapps/BankingApp
```

**Windows:**
```cmd
xcopy /E /I webapp "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\BankingApp"
```

### Method 2: Create WAR File

```bash
# Navigate to webapp directory
cd webapp

# Create WAR file
jar -cvf BankingApp.war *

# Copy WAR to Tomcat webapps
cp BankingApp.war /path/to/tomcat/webapps/
```

---

## Step 5: Start Tomcat

### Linux

```bash
# Start Tomcat
/opt/tomcat/bin/startup.sh

# Check logs
tail -f /opt/tomcat/logs/catalina.out
```

### Windows

```cmd
REM Start Tomcat
"C:\Program Files\Apache Software Foundation\Tomcat 9.0\bin\startup.bat"

REM Or use Windows Service
net start Tomcat9
```

---

## Step 6: Access the Application

Open your browser and navigate to:

```
http://localhost:8080/BankingApp/
```

You should see the Banking Application home page!

---

## Step 7: Verify Servlet Lifecycle

### Check Console Logs

Look for these messages in Tomcat logs:

```
[LIFECYCLE] init() called
[LIFECYCLE] AccountServlet initialized at: ...
[DatabaseUtil] Database 'bankdb' is ready!
[DatabaseUtil] Table 'accounts' is ready!
```

### Test Lifecycle

1. Access: `http://localhost:8080/BankingApp/AccountServlet`
2. Check console - you'll see `service()` called
3. Refresh page multiple times - request count increases
4. Stop Tomcat - you'll see `destroy()` called

---

## Troubleshooting

### Error: "HTTP Status 404 - Not Found"

**Cause:** Application not deployed correctly

**Solution:**
1. Check that `BankingApp` folder exists in `webapps`
2. Verify folder structure:
   ```
   webapps/BankingApp/
   ├── index.html
   ├── css/
   └── WEB-INF/
       ├── web.xml
       ├── classes/
       └── lib/
   ```

---

### Error: "ClassNotFoundException: com.banking.servlets.AccountServlet"

**Cause:** Servlets not compiled or in wrong location

**Solution:**
1. Verify compiled classes exist:
   ```
   webapps/BankingApp/WEB-INF/classes/com/banking/servlets/AccountServlet.class
   ```
2. Recompile if missing
3. Check package names match directory structure

---

### Error: "java.sql.SQLException: No suitable driver"

**Cause:** MySQL JDBC driver not found

**Solution:**
1. Verify JAR exists:
   ```
   webapps/BankingApp/WEB-INF/lib/mysql-connector-j-8.0.33.jar
   ```
2. Copy JAR if missing:
   ```bash
   cp mysql-connector-j-8.0.33.jar /path/to/tomcat/webapps/BankingApp/WEB-INF/lib/
   ```

---

### Error: "Communications link failure"

**Cause:** MySQL server not running

**Solution:**
```bash
# Linux
sudo systemctl start mysql

# Windows
net start MySQL80
```

---

### Error: "Access denied for user 'root'@'localhost'"

**Cause:** Wrong database password

**Solution:**
1. Update password in `DatabaseUtil.java`
2. Recompile
3. Restart Tomcat

---

## Directory Structure (After Deployment)

```
webapps/BankingApp/
├── index.html
├── createAccount.html
├── viewAccount.html
├── deposit.html
├── withdraw.html
├── transfer.html
├── css/
│   └── style.css
└── WEB-INF/
    ├── web.xml
    ├── classes/
    │   └── com/
    │       └── banking/
    │           ├── model/
    │           │   └── Account.class
    │           ├── dao/
    │           │   ├── DatabaseUtil.class
    │           │   └── AccountDAO.class
    │           └── servlets/
    │               ├── AccountServlet.class
    │               ├── CreateAccountServlet.class
    │               ├── ViewAccountServlet.class
    │               ├── DepositServlet.class
    │               ├── WithdrawServlet.class
    │               ├── TransferServlet.class
    │               └── LogoutServlet.class
    └── lib/
        └── mysql-connector-j-8.0.33.jar
```

---

## Testing Checklist

After deployment, test all features:

- [ ] Home page loads
- [ ] Create account (test doPost)
- [ ] View account (test doGet with query parameters)
- [ ] Deposit money (test JDBC update)
- [ ] Withdraw money (test validation)
- [ ] Transfer funds (test RequestDispatcher)
- [ ] Logout (test SendRedirect)
- [ ] Lifecycle demo (check console logs)

---

## Stopping Tomcat

### Linux
```bash
/opt/tomcat/bin/shutdown.sh
```

### Windows
```cmd
"C:\Program Files\Apache Software Foundation\Tomcat 9.0\bin\shutdown.bat"

REM Or
net stop Tomcat9
```

---

## Quick Deployment Script (Linux)

Create `deploy.sh`:

```bash
#!/bin/bash

# Configuration
TOMCAT_HOME="/opt/tomcat"
PROJECT_DIR="/path/to/Banking_management_system"
APP_NAME="BankingApp"

# Compile
echo "Compiling servlets..."
javac -cp "$TOMCAT_HOME/lib/servlet-api.jar:$PROJECT_DIR/webapp/WEB-INF/lib/mysql-connector-j-8.0.33.jar" \
      -d "$PROJECT_DIR/webapp/WEB-INF/classes" \
      $PROJECT_DIR/src/com/banking/model/*.java \
      $PROJECT_DIR/src/com/banking/dao/*.java \
      $PROJECT_DIR/src/com/banking/servlets/*.java

# Stop Tomcat
echo "Stopping Tomcat..."
$TOMCAT_HOME/bin/shutdown.sh

# Deploy
echo "Deploying application..."
rm -rf $TOMCAT_HOME/webapps/$APP_NAME
cp -r $PROJECT_DIR/webapp $TOMCAT_HOME/webapps/$APP_NAME

# Start Tomcat
echo "Starting Tomcat..."
$TOMCAT_HOME/bin/startup.sh

echo "Deployment complete!"
echo "Access at: http://localhost:8080/$APP_NAME/"
```

Make executable and run:
```bash
chmod +x deploy.sh
./deploy.sh
```

---

**🎉 Your servlet application is now deployed and running!**
