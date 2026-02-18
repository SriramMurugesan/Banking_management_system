# Universal Setup Guide for Banking Web App

This guide explains how to set up the development environment and run the Banking Web Application on **Windows**, **macOS**, and **Linux**.

---

## 1. Prerequisites (All OS)

Before starting, ensure you have the following installed:

1.  **Java Development Kit (JDK) 8 or higher**
    -   Verify by running: `java -version` in your terminal/command prompt.
2.  **VS Code (Visual Studio Code)**
    -   Recommended extensions: "Extension Pack for Java" by Microsoft.
3.  **MySQL Server**
    -   Ensure the service is running and you have a root user (password: `root123` based on the code, or update `DBConnection.java`).

---

## 2. Setting Up Apache Tomcat

Apache Tomcat is the web server used to run our Servlet-based application.

### A. Windows
1.  **Download**: Go to [Tomcat 9 Download Page](https://tomcat.apache.org/download-90.cgi).
2.  **Select Version**: Under "Binary Distributions" -> "Core", download the **"32-bit/64-bit Windows Service Installer"** or the **"zip"** archive.
3.  **Install/Extract**:
    -   If using the Installer, follow the wizard.
    -   If using Zip, extract it to a folder (e.g., `C:\apache-tomcat-9.0.x`).
4.  **Environment Variables (Optional)**:
    -   Set `CATALINA_HOME` to your Tomcat folder path.

### B. macOS / Linux
1.  **Download**: Go to [Tomcat 9 Download Page](https://tomcat.apache.org/download-90.cgi).
2.  **Select Version**: Under "Binary Distributions" -> "Core", download the **"tar.gz"** archive.
3.  **Extract**:
    -   Open terminal and navigate to the download location.
    -   Run: `tar -xvf apache-tomcat-9.0.xx.tar.gz`
    -   Move the folder to a desired location (e.g., `~/tomcat` or `/opt/tomcat`).
4.  **Permissions**:
    -   Make scripts executable: `chmod +x /path/to/tomcat/bin/*.sh`

---

## 3. Configuring VS Code (All OS)

To avoid "package javax.servlet not exist" errors, you need to link the Servlet API jar.

1.  **Create `.vscode` Folder**: inside your project root.
2.  **Create `settings.json`**: inside `.vscode`.
3.  **Add Configuration**:
    ```json
    {
        "java.project.referencedLibraries": [
            "lib/**/*.jar",
            "path/to/mysql-connector-j-8.0.33.jar", 
            "path/to/tomcat/lib/servlet-api.jar" 
        ]
    }
    ```
    *Note: Replace paths with your actual locations. On Windows, use double backslashes `\\`.*

---

## 4. Deployment & Running

### A. Deploying the App
1.  **Locate Webapps Folder**: Find the `webapps` directory inside your Tomcat installation.
2.  **Copy Project**: Copy the `BankingApp` folder (which contains `WEB-INF`, `index.html`, etc.) into `webapps`.
    -   Final path should look like: `.../tomcat/webapps/BankingApp/`
3.  **Check Libraries**: Ensure `WEB-INF/lib` inside `BankingApp` contains `mysql-connector-j-8.0.33.jar`.

### B. Starting the Server

**Windows:**
1.  Open Command Prompt (cmd) or PowerShell.
2.  Navigate to `C:\path\to\tomcat\bin`.
3.  Run: `startup.bat`

**macOS / Linux:**
1.  Open Terminal.
2.  Navigate to `/path/to/tomcat/bin`.
3.  Run: `./startup.sh`

### C. Accessing the Application
Open your browser and go to:
-   [http://localhost:8080/BankingApp/](http://localhost:8080/BankingApp/)

---

## 5. Stopping the Server

**Windows:**
-   Run `shutdown.bat` in the `bin` folder.

**macOS / Linux:**
-   Run `./shutdown.sh` in the `bin` folder.

---

## Troubleshooting

-   **Port 8080 in use**:
    -   Edit `conf/server.xml` in Tomcat folder.
    -   Change `<Connector port="8080" ...>` to another port like `8081`.
-   **Class Not Found (JDBC)**:
    -   Ensure `mysql-connector` jar is in `BankingApp/WEB-INF/lib/`.
-   **404 Not Found**:
    -   Check if the folder name in `webapps` matches the URL path (Case Sensitive on Linux/macOS).
    -   Check Tomcat logs in `logs/catalina.out` for deployment errors.
