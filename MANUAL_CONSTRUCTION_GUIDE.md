# Manual Construction & Verification Guide

If you want to build and deploy the application **completely manually** (without my automation), follow these steps.

---

## Part 1: Building the App from Scratch

### 1. Create the Directory Structure
First, creates the folders for the web application.
```bash
mkdir -p BankingApp/WEB-INF/classes
mkdir -p BankingApp/WEB-INF/lib
```

### 2. Copy Web Files
Copy your HTML and CSS files to the `BankingApp` folder.
*(Assuming you are in the project root)*
```bash
cp webapp/*.html BankingApp/
cp webapp/*.css BankingApp/
```

### 3. Copy Libraries
Copy the MySQL Connector JAR to the `lib` folder.
```bash
cp mysql-connector-j-8.0.33.jar BankingApp/WEB-INF/lib/
```

### 4. Compile Java Code
Compile your Servlets and DAO classes. You need to include the Servlet API and MySQL JAR in the classpath (`-cp`).
*(Note: Replace `/path/to/servlet-api.jar` with the real path, e.g., inside Tomcat's `lib` folder)*

```bash
javac -cp .:mysql-connector-j-8.0.33.jar:/path/to/tomcat/lib/servlet-api.jar -d BankingApp/WEB-INF/classes DBConnection.java AccountDAO.java LifeCycleServlet.java AccountServlet.java TransactionServlet.java
```
*If successful, this creates `.class` files inside `BankingApp/WEB-INF/classes`.*

---

## Part 2: Deploying Manually

### 1. Move to Tomcat
Copy your `BankingApp` folder into Tomcat's `webapps` directory.
```bash
cp -r BankingApp tomcat/webapps/
```

### 2. Start Server
```bash
./tomcat/bin/startup.sh
```

---

## Part 3: How to Check if it's Running

### 1. Check the Logs (Best Method)
If something goes wrong, the browser just says "404 Not Found". To know **why**, check the logs:
```bash
tail -f tomcat/logs/catalina.out
```
*Look for "BankingApp" in the logs. If you see "Deployed Deployment Descriptor", it worked.*

### 2. Check the Browser
Open: [http://localhost:8080/BankingApp/](http://localhost:8080/BankingApp/)

-   **If you see the Menu**: It works!
-   **If "404 Not Found"**: The server is running, but it can't find your app (check folder name in `webapps`).
-   **If "Connection Refused"**: The server is NOT running (check logs).
