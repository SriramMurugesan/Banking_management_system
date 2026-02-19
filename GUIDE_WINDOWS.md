# 🎓 Teacher's Guide: Connecting HTML, Servlets & Tomcat (Windows)

**Target Audience**: Absolute Beginners  
**OS**: Windows 10/11  
**Goal**: Create a web page, send data to a Java Servlet, and run it on a Tomcat Server.

---

## 🛠️ Phase 1: The Setup (One-Time)

Before coding, students need the tools.

### 1. Install Java (JDK)
*   **Why?** Servlets are Java code. We need Java to compile and run them.
*   **Action**: Download and install **JDK 17** (or 8+).
*   **Verify**: Open Command Prompt (`cmd`) and type `java -version`.

### 2. Download Tomcat (The Server)
*   **Why?** Regular Java runs on your desktop. Web apps need a "Server" to listen for browser requests.
*   **Action**:
    1.  Go to [tomcat.apache.org](https://tomcat.apache.org/download-90.cgi).
    2.  Download **"64-bit Windows zip"** (Tomcat 9).
    3.  **Extract** it to `C:\tomcat9` (Keep the path simple!).

### 3. Environment Variables (Critical for Windows)
*   **Why?** So Windows knows where Java is when we run commands like `javac`.
*   **Action**:
    1.  Search "Env" in Start Menu -> "Edit the system environment variables".
    2.  Click "Environment Variables".
    3.  Under **System Variables**, find `Path` -> Edit -> New.
    4.  Add the path to your JDK `bin` folder (e.g., `C:\Program Files\Java\jdk-17\bin`).
    5.  Click OK.

---

## 📁 Phase 2: Project Structure (The "Skeleton")

Tomcat demands a specific folder structure. If you don't follow it, it won't work.

**Action**: Create a folder named `MyFirstApp` on Desktop. Inside it, create folders exactly like this:

```
MyFirstApp/
├── index.html              <-- The Frontend (User sees this)
└── WEB-INF/
    ├── classes/            <-- The Backend (Compiled Java code goes here)
    └── lib/                <-- Libraries (servlet-api.jar goes here)
```

---

## ✍️ Phase 3: The Coding

### Step 1: The HTML (Frontend)
Create `index.html` in `MyFirstApp` folder.

```html
<!-- index.html -->
<!DOCTYPE html>
<html>
<body>
    <h2>User Login</h2>
    <!-- ACTION: Where to send data? To the URL pattern '/login' -->
    <!-- METHOD: How to send? POST (securely inside request) -->
    <form action="login" method="POST">
        Name: <input type="text" name="username"> <br>
        <button type="submit">Submit</button>
    </form>
</body>
</html>
```
**Explanation for Students**:
*   `action="login"`: This tells the browser "When clicked, send this data to the program named `login`".

### Step 2: The Servlet (Backend)
Create `LoginServlet.java` (anywhere, e.g., in `MyFirstApp`).

```java
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

// @WebServlet("/login") -> This "connects" the HTML action="login" to this Java Class!
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    // Using doPost because HTML form said method="POST"
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Get data from HTML
        String user = request.getParameter("username");
        
        // 2. Prepare response (HTML)
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // 3. Send HTML back
        out.println("<h1>Hello, " + user + "!</h1>");
        out.println("<p>Request received successfully by Tomcat.</p>");
    }
}
```

---

## ⚙️ Phase 4: The Connection (Compiling)

This is where most beginners get stuck. We need to turn `.java` (Human code) into `.class` (Machine/Tomcat code).

**The Challenge**: `LoginServlet` uses special Tomcat code (`javax.servlet`). Standard Java doesn't know what that is.

**The Fix**: We must tell the compiler *where* to find Tomcat's libraries.

**Command (Run in `cmd` inside `MyFirstApp`):**
```cmd
javac -cp "C:\tomcat9\lib\servlet-api.jar" -d WEB-INF\classes LoginServlet.java
```

**Breakdown**:
*   `javac`: Java Compiler.
*   `-cp "..."`: **Class Path**. "Hey Compiler, borrow `servlet-api.jar` from Tomcat to understand what `HttpServlet` is."
*   `-d WEB-INF\classes`: "Put the resulting `.class` file into this specific folder."
*   `LoginServlet.java`: The file to compile.

---

## 🚀 Phase 5: Deployment & Running

### 1. Deploy
Copy the entire `MyFirstApp` folder into Tomcat's `webapps` folder.
*   Source: `Desktop\MyFirstApp`
*   Destination: `C:\tomcat9\webapps\MyFirstApp`

### 2. Start Server
1.  Open `cmd`.
2.  Go to Tomcat bin: `cd C:\tomcat9\bin`
3.  Run: `startup.bat`
4.  A new window should pop up properly designated as Tomcat.

### 3. Test
Open Chrome and go to:
`http://localhost:8080/MyFirstApp/index.html`

1.  You see the form.
2.  Type a name (e.g., "Student").
3.  Click Submit.
4.  **Magic**: The URL changes to `/login`, and you see "Hello, Student!".

---

## 🐛 Troubleshooting for Students

| Problem | Cause | Solution |
| :--- | :--- | :--- |
| **"javac is not recognized"** | Java not in PATH. | Re-do Phase 1 Step 3. |
| **"package javax.servlet does not exist"** | Missing `-cp` flag. | Check the path to `servlet-api.jar` in compile command. |
| **404 Not Found** | Wrong URL or Folder Name. | URL must match folder name in `webapps` exactly. Case sensitive! |
| **405 Method Not Allowed** | `doGet` vs `doPost`. | HTML `method="POST"` must match Servlet `doPost`. |
