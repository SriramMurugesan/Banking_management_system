# Complete Windows Setup Guide - Java + MySQL + JDBC

This guide will walk you through the complete setup process for running Java applications with MySQL database on Windows.

---

## Table of Contents
1. [MySQL Installation](#1-mysql-installation)
2. [MySQL Server Configuration](#2-mysql-server-configuration)
3. [Download JDBC Driver](#3-download-jdbc-driver)
4. [Setup Java Project](#4-setup-java-project)
5. [Test Connection](#5-test-connection)
6. [Troubleshooting](#troubleshooting)

---

## 1. MySQL Installation

### Step 1.1: Download MySQL Installer

1. Go to the official MySQL website: https://dev.mysql.com/downloads/installer/
2. Click on **"Download"** for **MySQL Installer for Windows**
3. Choose the **mysql-installer-community** version (larger file, ~400MB)
4. Click **"No thanks, just start my download"** (no login required)

![MySQL Download Page](https://dev.mysql.com/downloads/installer/)

### Step 1.2: Run MySQL Installer

1. **Double-click** the downloaded `.msi` file
2. If Windows asks "Do you want to allow this app to make changes?", click **Yes**

### Step 1.3: Choose Setup Type

1. Select **"Developer Default"** (recommended for learning)
   - This includes MySQL Server, Workbench, and other useful tools
2. Click **Next**

### Step 1.4: Check Requirements

1. The installer will check for required software (Visual C++, etc.)
2. Click **Execute** to install any missing requirements
3. Wait for all requirements to install
4. Click **Next**

### Step 1.5: Installation

1. Review the products to be installed
2. Click **Execute** to start installation
3. Wait for all products to install (this may take 5-10 minutes)
4. Click **Next** when all products show green checkmarks

---

## 2. MySQL Server Configuration

### Step 2.1: Type and Networking

1. **Config Type**: Select **Development Computer**
2. **Port**: Keep default **3306**
3. **Open Windows Firewall**: Check this box
4. Click **Next**

### Step 2.2: Authentication Method

1. Select **"Use Strong Password Encryption"** (recommended)
2. Click **Next**

### Step 2.3: Set Root Password

> ⚠️ **IMPORTANT**: Remember this password! You'll need it to connect from Java.

1. Enter a password for the **root** user (e.g., `root123`)
2. Re-enter the password to confirm
3. (Optional) You can add other users, but root is sufficient for learning
4. Click **Next**

**Example:**
```
Root Password: root123
Confirm Password: root123
```

### Step 2.4: Windows Service

1. **Configure as Windows Service**: Keep checked
2. **Service Name**: Keep default **MySQL80**
3. **Start at System Startup**: Keep checked
4. Click **Next**

### Step 2.5: Apply Configuration

1. Click **Execute** to apply all configurations
2. Wait for all steps to complete (green checkmarks)
3. Click **Finish**
4. Click **Next** on the main installer

### Step 2.6: Complete Installation

1. Click **Finish** to close the installer
2. MySQL Server is now installed and running!

---

## 3. Download JDBC Driver

The JDBC driver is a JAR file that allows Java to communicate with MySQL.

### Method 1: Direct Download (Recommended)

1. Go to: https://dev.mysql.com/downloads/connector/j/
2. Select **Platform Independent** from the dropdown
3. Download the **ZIP Archive** (e.g., `mysql-connector-j-8.0.33.zip`)
4. Click **"No thanks, just start my download"**
5. **Extract the ZIP file** to a folder
6. Inside, you'll find `mysql-connector-j-8.0.33.jar`
7. **Copy this JAR file** to your project folder

### Method 2: Maven Repository (Alternative)

1. Go to: https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/
2. Choose a version (e.g., `8.0.33/`)
3. Download the `.jar` file directly
4. **Copy this JAR file** to your project folder

### Where to Place the JAR File

```
Your_Project_Folder/
├── Bankingapp.java
├── mysql-connector-j-8.0.33.jar  ← Place the JAR here
└── other files...
```

---

## 4. Setup Java Project

### Step 4.1: Verify MySQL is Running

1. Press **Windows + R**
2. Type `services.msc` and press Enter
3. Look for **MySQL80** in the list
4. Status should be **Running**
5. If not running, right-click → **Start**

### Step 4.2: Test MySQL Connection (Optional but Recommended)

1. Open **MySQL Workbench** (installed with MySQL)
2. Click on **Local instance MySQL80**
3. Enter your root password (e.g., `root123`)
4. If connected successfully, you'll see the database interface

### Step 4.3: Update Java Code

Open your `Bankingapp.java` and update the database credentials:

```java
// Database connection details
static final String DB_URL = "jdbc:mysql://localhost:3306/bankdb";
static final String DB_USER = "root";
static final String DB_PASSWORD = "root123";  // ← Your MySQL password
```

### Step 4.4: Compile the Java Program

Open **Command Prompt** in your project folder:

1. Press **Windows + R**
2. Type `cmd` and press Enter
3. Navigate to your project folder:
   ```cmd
   cd C:\Users\YourName\Desktop\Banking_management_system
   ```
4. Compile the program:
   ```cmd
   javac Bankingapp.java
   ```

### Step 4.5: Run the Java Program

**Important**: You must include the JDBC driver in the classpath!

```cmd
java -cp ".;mysql-connector-j-8.0.33.jar" BankingApp
```

> **Note**: On Windows, use semicolon `;` instead of colon `:` to separate classpath entries.

---

## 5. Test Connection

### Expected Output

If everything is set up correctly, you should see:

```
Database 'bankdb' is ready!
Table 'accounts' is ready!
Database initialized successfully!
Loaded 0 accounts from database.

====BANK MENU====
1. Create Account
2. Deposit
3. Withdraw
4. Show Details
5. Apply Interest
6. Exit
Enter your choice:
```

### What This Means

✅ **Database created automatically** - The `bankdb` database was created  
✅ **Table created automatically** - The `accounts` table was created  
✅ **Connection successful** - Java successfully connected to MySQL  
✅ **Application ready** - You can now use all features!

---

## Troubleshooting

### Error: "No suitable driver found for jdbc:mysql://localhost:3306/"

**Cause**: JDBC driver not in classpath

**Solution**:
1. Make sure `mysql-connector-j-8.0.33.jar` is in your project folder
2. Use the correct run command:
   ```cmd
   java -cp ".;mysql-connector-j-8.0.33.jar" BankingApp
   ```
3. Note the semicolon `;` on Windows (not colon `:`)

### Error: "Access denied for user 'root'@'localhost'"

**Cause**: Wrong password

**Solution**:
1. Check your MySQL root password
2. Update the password in `Bankingapp.java`:
   ```java
   static final String DB_PASSWORD = "your_actual_password";
   ```

### Error: "Communications link failure"

**Cause**: MySQL Server is not running

**Solution**:
1. Press **Windows + R**, type `services.msc`
2. Find **MySQL80** service
3. Right-click → **Start**

### Error: "javac is not recognized as an internal or external command"

**Cause**: Java not in PATH

**Solution**:
1. Install JDK if not installed
2. Add Java to PATH:
   - Right-click **This PC** → **Properties**
   - Click **Advanced system settings**
   - Click **Environment Variables**
   - Under **System Variables**, find **Path**
   - Click **Edit** → **New**
   - Add: `C:\Program Files\Java\jdk-XX\bin` (replace XX with your version)
   - Click **OK** on all windows
   - **Restart Command Prompt**

### MySQL Server Won't Start

**Solution 1**: Restart the service
```cmd
net stop MySQL80
net start MySQL80
```

**Solution 2**: Check port 3306 is not in use
```cmd
netstat -ano | findstr :3306
```

---

## Quick Reference Card

### Compile Command
```cmd
javac Bankingapp.java
```

### Run Command (Windows)
```cmd
java -cp ".;mysql-connector-j-8.0.33.jar" BankingApp
```

### Check MySQL Service
```cmd
net start MySQL80
```

### MySQL Connection Details
- **Host**: localhost
- **Port**: 3306
- **Database**: bankdb (created automatically)
- **Username**: root
- **Password**: (your password)

---

## Understanding JDBC Connection

### What is JDBC?

**JDBC** (Java Database Connectivity) is an API that allows Java programs to interact with databases.

```
Java Application
      ↓
  JDBC Driver (mysql-connector-j-8.0.33.jar)
      ↓
  MySQL Server
      ↓
  Database (bankdb)
```

### Connection String Breakdown

```java
jdbc:mysql://localhost:3306/bankdb
│    │      │         │     │
│    │      │         │     └─ Database name
│    │      │         └─────── Port number
│    │      └───────────────── Server address (localhost = your computer)
│    └──────────────────────── Database type (MySQL)
└───────────────────────────── Protocol (JDBC)
```

### Why Do We Need the JAR File?

The JAR file (`mysql-connector-j-8.0.33.jar`) contains the **MySQL JDBC Driver** code that:
- Translates Java commands to MySQL commands
- Handles network communication with MySQL Server
- Manages connections, queries, and results

Without this JAR file, Java doesn't know how to talk to MySQL!

---

## Additional Resources

- **MySQL Documentation**: https://dev.mysql.com/doc/
- **JDBC Tutorial**: https://docs.oracle.com/javase/tutorial/jdbc/
- **MySQL Workbench Guide**: https://dev.mysql.com/doc/workbench/en/

---

## Summary Checklist

Before running your Java application, make sure:

- [ ] MySQL Server is installed
- [ ] MySQL Server is running (check services.msc)
- [ ] You know your root password
- [ ] JDBC driver JAR is in your project folder
- [ ] Database credentials are correct in Java code
- [ ] You compile with: `javac Bankingapp.java`
- [ ] You run with: `java -cp ".;mysql-connector-j-8.0.33.jar" BankingApp`

---

**🎉 Congratulations!** You're now ready to build Java applications with MySQL database!
