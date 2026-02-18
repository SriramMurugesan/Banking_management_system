# Simple Banking Web Application

## Architecture Overview

This is a simple web-based banking application built using Java Servlets, JDBC, HTML, and CSS.

### Components:
1.  **Frontend (View)**:
    -   `index.html`: Landing page with menu.
    -   `create_account.html`: Form to create a new bank account.
    -   `transaction.html`: Form to deposit or withdraw money.
    -   `style.css`: Stylesheet for a clean and simple UI.
    -   `success.html`: Confirmation page for successful operations.

2.  **Backend (Controller)**:
    -   `LifeCycleServlet.java`: Demonstrates Servlet lifecycle methods (`init`, `service`, `destroy`).
    -   `AccountServlet.java`: Handles account creation (`doPost`) and viewing details (`doGet`).
    -   `TransactionServlet.java`: Handles deposits and withdrawals (`doPost`) and uses `RequestDispatcher` for forwarding.

3.  **Database (Model)**:
    -   `DBConnection.java`: Singleton-style class to manage JDBC connections.
    -   `AccountDAO.java`: Data Access Object to perform SQL operations (INSERT, UPDATE, SELECT).
    -   `MySQL Database`: Stores account information in the `accounts` table.

## Prerequisites

1.  **Java Development Kit (JDK)** (installed).
2.  **Apache Tomcat** (or any Servlet Container).
3.  **MySQL Server** (running).
4.  **MySQL JDBC Driver** (included in project: `mysql-connector-j-8.0.33.jar`).
5.  **Servlet API** (usually provided by Tomcat in `lib/servlet-api.jar`).

## Setup & Running Instructions

### 1. Database Setup
Ensure your MySQL server is running and the database is initialized. You can run the existing `BankingApp.java` once to initialize the database, or use the provided `setup_database.sql` script.

### 2. Compilation
You need to compile the Java files and place the `.class` files in the `WEB-INF/classes` directory of your web application structure.

**Command to compile (Linux/Mac):**
Assuming you have the `servlet-api.jar` (standard location in Tomcat `lib` folder):
```bash
javac -cp .:mysql-connector-j-8.0.33.jar:/path/to/tomcat/lib/servlet-api.jar -d webapp/WEB-INF/classes DBConnection.java AccountDAO.java LifeCycleServlet.java AccountServlet.java TransactionServlet.java
```
*Note: Replace `/path/to/tomcat/lib/servlet-api.jar` with the actual path to your Tomcat's servlet API jar.*

### 3. Deployment & Running (Simplified)

I have downloaded Apache Tomcat 9 for you in the `tomcat` directory.

1.  **Start Server**:
    -   Open terminal in the project folder.
    -   Run: `./tomcat/bin/startup.sh`
2.  **Access App**:
    -   Open browser: `http://localhost:8080/BankingApp/`
3.  **Stop Server**:
    -   Run: `./tomcat/bin/shutdown.sh`

### Manual Deployment (If using your own Tomcat)
1.  **Copy Folder**: Copy the `BankingApp` folder to your Tomcat's `webapps` directory.
2.  **Verify Library**: Ensure `WEB-INF/lib/mysql-connector-j-8.0.33.jar` exists.

### 4. Start Server
Start your Tomcat server (e.g., `./startup.sh` in Tomcat `bin` folder).

## Testing Endpoints

Open your browser and navigate to: `http://localhost:8080/BankingApp/`

### 1. Create Account
-   **URL**: Click "Create Account" on the home page.
-   **Action**: Fill in Account Number, Name, Balance, Email, and Type. Click "Create".
-   **Expected Result**: Database record created, redirected to `success.html`.

### 2. Deposit/Withdraw
-   **URL**: Click "Deposit / Withdraw" on the home page.
-   **Action**: Enter Account Number, Amount, and select Type (Deposit/Withdraw). Click "Submit".
-   **Expected Result**: Balance updated in database, forwarded to `success.html`.

### 3. View Details
-   **URL**: "View Details" section on the home page.
-   **Action**: Enter Account Number and click "View Details".
-   **Expected Result**: Displays account information or error if not found.

### 4. Lifecycle Demo
-   **URL**: `http://localhost:8080/BankingApp/lifecycle`
-   **Action**: Visit the URL. Refresh the page.
-   **Expected Result**: Check your Tomcat/Server console logs. You should see "init()", "service()", etc., printed.

## Directory Structure (Deployment)
```
BankingApp/
├── index.html
├── style.css
├── create_account.html
├── transaction.html
├── success.html
├── WEB-INF/
│   ├── classes/
│   │   ├── DBConnection.class
│   │   ├── AccountDAO.class
│   │   ├── LifeCycleServlet.class
│   │   ├── AccountServlet.class
│   │   └── TransactionServlet.class
│   └── lib/
│       └── mysql-connector-j-8.0.33.jar
```
