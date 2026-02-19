# Windows Servlet Setup Guide (From Scratch)

This guide provides a **complete, step-by-step** process to set up the Banking Web Application on Windows. It covers everything from installing the software to running the app in your browser.

---

## 1. Prerequisites (What you need)

Before we start, you need to download and install these 3 things:

### 1. Java Development Kit (JDK)
*   **Download**: [Oracle JDK 17 or 21](https://www.oracle.com/java/technologies/downloads/)
*   **Install**: Run the installer and click Next until finished.
*   **Verify**: Open Command Prompt (`cmd`) and type: `java -version`. You should see version 17 or higher.

### 2. Apache Tomcat (The Server)
*   **Download**: [Tomcat 10 (zip)](https://tomcat.apache.org/download-10.cgi) (Look for "Core" -> "64-bit Windows zip")
*   **Install**:
    1.  Extract the ZIP file to a simple folder, e.g., `C:\tomcat`.
    2.  *Do not* hide it deep in your Downloads folder.

### 3. MySQL Server (The Database)
*   Because setting up MySQL is complex, please follow the [WINDOWS_SETUP_GUIDE.md](WINDOWS_SETUP_GUIDE.md) we already created.
*   Make sure you know your **root password**.

---

## 2. Environment Setup

We need to tell Windows where Java and Tomcat are so we can run commands easily.

1.  Press **Windows Key** and type "env", then select **Edit the system environment variables**.
2.  Click **Environment Variables** (bottom right button).
3.  Under **System Variables** (bottom box):
    *   Click **New**. Name: `JAVA_HOME`. Value: Path to your JDK (e.g., `C:\Program Files\Java\jdk-17`).
    *   Click **New**. Name: `CATALINA_HOME`. Value: Path to your Tomcat folder (e.g., `C:\tomcat`).
4.  Find the `Path` variable in the list, select it, and click **Edit**.
    *   Click **New** and add: `%JAVA_HOME%\bin`
    *   Click **OK**, **OK**, **OK** to close all windows.

---

## 3. Directory Structure

Create a new folder for your project (e.g., on Desktop named `BankingProject`). Inside, create this exact structure:

```text
BankingProject/
├── BankingApp/              (Main Web Folder)
│   ├── index.html           (Copy from source)
│   ├── create_account.html  (Copy from source)
│   ├── ...                  (Other HTML/CSS files)
│   └── WEB-INF/             (Specific Folder Name - All Caps)
│       ├── lib/             (Libraries)
│       │   └── mysql-connector-j-8.0.33.jar  <-- PASTE JAR HERE
│       └── classes/         (Compiled Code)
│           └── (Leave Empty for now, we will fill it later)
├── src/                     (Source Code)
│   ├── AccountDAO.java
│   ├── AccountServlet.java
│   ├── TransactionServlet.java
│   ├── LifeCycleServlet.java
│   └── DBConnection.java
```

> **CRITICAL STEP**: You MUST download the MySQL Connector JAR (see `WINDOWS_SETUP_GUIDE.md`) and paste it into `BankingApp\WEB-INF\lib`.

---

## 4. Database Setup

1.  Open **MySQL Workbench**.
2.  Connect to your Local instance.

3.  Open the `setup_database.sql` file (File -> Open SQL Script).
4.  Click the **Lightning Bolt** icon to run it.
5.  This creates the `bankdb` database and `accounts` table.

---

## 5. Compiling the Code (The Tricky Part)

This is where most people get stuck. We need to compile the `.java` files into `.class` files using the Tomcat library.

1.  Open **Command Prompt** (`cmd`).
2.  Navigate to your project folder:
    ```cmd
    cd C:\Users\YourName\Desktop\BankingProject
    ```
    *(Tip: You can shift + right-click in the folder and select "Open PowerShell window here" or "Open in Terminal")*

3.  **Run this EXACT command**:

    ```cmd
    javac -cp ".;%CATALINA_HOME%\lib\servlet-api.jar" -d BankingApp\WEB-INF\classes src\*.java
    ```

    **Breakdown**:
    *   `javac`: The Java compiler.
    *   `-cp ".;..."`: Classpath. Tells Java where to look for definitions.
        *   `.`: Look in current folder.
        *   `;`: Separator for Windows.
        *   `%CATALINA_HOME%\lib\servlet-api.jar`: The Tomcat library that understands `@WebServlet` and `HttpServlet`.
    *   `-d BankingApp\WEB-INF\classes`: **Destination**. Puts compiled files straight into the correct folder.
    *   `src\*.java`: Compiles all Java files in the `src` folder.

    **Success?** If it runs silently (no errors), it worked! Check `BankingApp\WEB-INF\classes` - you should see `.class` files now.

---

## 6. Deploying to Tomcat

1.  Copy the entire `BankingApp` folder from your project.
2.  Go to your Tomcat folder (e.g., `C:\tomcat`).
3.  Open the `webapps` folder.
4.  Paste `BankingApp` here.

    Result should look like: `C:\tomcat\webapps\BankingApp`

---

## 7. Running the Application

1.  Open **Command Prompt**.
2.  Start Tomcat:
    ```cmd
    %CATALINA_HOME%\bin\startup.bat
    ```
    (A new window should pop up with Tomcat logs).
3.  Open your Web Browser (Chrome, Edge, etc.).
4.  Go to:
    **[http://localhost:8080/BankingApp](http://localhost:8080/BankingApp)**

---

## Troubleshooting

### "javac is not recognized"
*   You probably didn't set the `Path` variable in Step 2.
*   Try closing and reopening the Command Prompt.

### "package javax.servlet does not exist"
*   Your `-cp` part of the compile command is wrong.
*   Make sure `%CATALINA_HOME%` is set correctly.
*   Try using the full path instead: `-cp ".;C:\tomcat\lib\servlet-api.jar"`

### "404 Not Found" in Browser
*   Did you paste `BankingApp` into `webapps`?
*   Is the folder named exactly `BankingApp`?
*   Did you compile the classes into `WEB-INF\classes`? (If that folder is missing or empty, it won't work).

### "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
*   You forgot to put the `mysql-connector-jar` into `BankingApp\WEB-INF\lib`.
*   Or you forgot to restart Tomcat after adding it (Shutdown -> Startup).
