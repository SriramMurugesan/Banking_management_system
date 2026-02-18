# Setup Summary: Automated vs. Manual Steps

Here is a breakdown of what has been done for you automatically and what commands you need to run.

## ✅ What is ALREADY DONE (Automated)

You do **NOT** need to do these steps. I have already done them:

1.  **Downloaded Tomcat**: Apache Tomcat 9 is downloaded and extracted in the `tomcat` folder.
2.  **Created Project Structure**: The `BankingApp` folder is created with all HTML, CSS, and Java files.
3.  **Compiled Code**: All Java Servlets and DAO classes are compiled into `WEB-INF/classes`.
4.  **Configured Libraries**: The `mysql-connector` JAR is already placed in `WEB-INF/lib`.
5.  **Deployed App**: The `BankingApp` folder has been copied into `tomcat/webapps/`.

---

## 🛠️ What YOU Need to Do (Manual Commands)

You only need to run the following commands to start and stop the server.

### 1. Open Terminal
Open your terminal and make sure you are in the project folder:
```bash
cd /home/sriram/Desktop/Vidyavahini/Banking_management_system
```

### 2. Start the Server
Run this command to start Tomcat:
```bash
./tomcat/bin/startup.sh
```
*Wait for a few seconds for it to start.*

### 3. Access the App
Open your web browser and go to:
[http://localhost:8080/BankingApp/](http://localhost:8080/BankingApp/)

### 4. Stop the Server
When you are done, run this command:
```bash
./tomcat/bin/shutdown.sh
```

---

## 📂 Visual Breakdown

```
Banking_management_system/
├── tomcat/                  <-- [AUTO] I downloaded this.
│   ├── bins/
│   │   ├── startup.sh       <-- [MANUAL] You run this.
│   │   └── shutdown.sh      <-- [MANUAL] You run this.
│   └── webapps/
│       └── BankingApp/      <-- [AUTO] I deployed this here.
├── BankingApp/              <-- [AUTO] Source code I created.
├── DBConnection.java        <-- [AUTO] Source code I created.
└── ...
```
