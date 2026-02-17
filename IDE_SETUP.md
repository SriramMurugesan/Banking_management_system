# IDE Setup Guide - Fixing Error Messages

## Understanding the Errors

The errors you're seeing are **IDE configuration issues**, not actual code problems. The code is correct, but your IDE (VS Code/Eclipse) needs to be configured properly for servlet development.

## Two Main Issues

### 1. Package Declaration Mismatch

**Error:** `The declared package "com.banking.dao" does not match the expected package "src.com.banking.dao"`

**Cause:** Your IDE thinks `src` is part of the package name, but it's actually the source folder.

**Solution:** Configure `src` as the source root.

### 2. Missing Servlet API

**Error:** `The import javax.servlet cannot be resolved`

**Cause:** servlet-api.jar is not in your IDE's classpath.

**Solution:** Add servlet-api.jar to your project libraries.

---

## Fix for VS Code

### Step 1: Install Java Extension Pack

1. Open VS Code
2. Go to Extensions (Ctrl+Shift+X)
3. Search for "Extension Pack for Java"
4. Install it

### Step 2: Configure Source Folder

Create `.vscode/settings.json`:

```json
{
    "java.project.sourcePaths": ["src"],
    "java.project.outputPath": "webapp/WEB-INF/classes",
    "java.project.referencedLibraries": [
        "webapp/WEB-INF/lib/*.jar",
        "/path/to/tomcat/lib/servlet-api.jar"
    ]
}
```

**Replace** `/path/to/tomcat/lib/servlet-api.jar` with your actual Tomcat path.

### Step 3: Reload Window

1. Press `Ctrl+Shift+P`
2. Type "Reload Window"
3. Press Enter

---

## Fix for Eclipse

### Step 1: Create Dynamic Web Project

1. File → New → Dynamic Web Project
2. Project name: `Banking_management_system`
3. Target runtime: Apache Tomcat 9.0
4. Click Finish

### Step 2: Configure Build Path

1. Right-click project → Properties
2. Java Build Path → Source tab
3. Remove any incorrect source folders
4. Add Folder → Select `src`
5. Click OK

### Step 3: Add Servlet Library

1. Right-click project → Properties
2. Java Build Path → Libraries tab
3. Add Library → Server Runtime → Apache Tomcat 9.0
4. Click OK

---

## Quick Fix: Ignore IDE Errors

If you just want to compile and run without fixing IDE errors:

### Compile from Command Line

```bash
# Navigate to project
cd /home/sriram/Desktop/Vidyavahini/Banking_management_system

# Create output directory
mkdir -p webapp/WEB-INF/classes

# Compile (replace Tomcat path)
javac -cp "/opt/tomcat/lib/servlet-api.jar:webapp/WEB-INF/lib/mysql-connector-j-8.0.33.jar" \
      -d webapp/WEB-INF/classes \
      src/com/banking/model/*.java \
      src/com/banking/dao/*.java \
      src/com/banking/servlets/*.java
```

### Windows:

```cmd
mkdir webapp\WEB-INF\classes

javac -cp "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\servlet-api.jar;webapp\WEB-INF\lib\mysql-connector-j-8.0.33.jar" -d webapp\WEB-INF\classes src\com\banking\model\*.java src\com\banking\dao\*.java src\com\banking\servlets\*.java
```

---

## Verify Compilation

After compiling, check that .class files exist:

```bash
ls -R webapp/WEB-INF/classes/com/banking/
```

You should see:
```
webapp/WEB-INF/classes/com/banking/
├── dao/
│   ├── AccountDAO.class
│   └── DatabaseUtil.class
├── model/
│   └── Account.class
└── servlets/
    ├── AccountServlet.class
    ├── CreateAccountServlet.class
    ├── DepositServlet.class
    ├── LogoutServlet.class
    ├── TransferServlet.class
    ├── ViewAccountServlet.class
    └── WithdrawServlet.class
```

---

## Summary

**The errors are cosmetic** - they're just IDE warnings. The code will compile and run fine when you:

1. Use the correct classpath during compilation
2. Deploy to Tomcat
3. Access via browser

**To make IDE errors go away:**
- Configure `src` as source folder
- Add servlet-api.jar to project libraries

**Or just ignore them and compile from command line!**

---

## Next Steps

1. ✅ Ignore IDE errors (they're just configuration issues)
2. ✅ Compile using command line (see above)
3. ✅ Deploy to Tomcat
4. ✅ Test the application

See [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) for full deployment instructions!
