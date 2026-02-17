# 🚀 Quick Start Guide - Banking Application

## ✅ Your App is Running!

Your banking application is now deployed and running on Tomcat.

**Access it here:** http://localhost:8080/BankingApp/

---

## 📋 Quick Commands

### 1️⃣ Compile All Servlets
```bash
cd /home/sriram/Desktop/Vidyavahini/Banking_management_system
javac -cp "webapp/WEB-INF/lib/*" -d webapp/WEB-INF/classes \
  src/com/banking/model/*.java \
  src/com/banking/dao/*.java \
  src/com/banking/servlets/*.java
```

### 2️⃣ Deploy to Tomcat
```bash
# Copy compiled classes
sudo cp -r webapp/WEB-INF/classes/* /var/lib/tomcat9/webapps/BankingApp/WEB-INF/classes/

# Copy libraries (if needed)
sudo cp webapp/WEB-INF/lib/*.jar /var/lib/tomcat9/webapps/BankingApp/WEB-INF/lib/
```

### 3️⃣ Restart Tomcat
```bash
sudo systemctl restart tomcat9
```

### 4️⃣ Check Tomcat Status
```bash
sudo systemctl status tomcat9
```

### 5️⃣ View Tomcat Logs
```bash
sudo tail -f /var/lib/tomcat9/logs/catalina.out
```

---

## 🔄 Development Workflow

When you make changes to your servlet code:

1. **Compile** the servlets
2. **Deploy** to Tomcat webapps
3. **Restart** Tomcat
4. **Test** in browser

**One-liner to do all:**
```bash
cd /home/sriram/Desktop/Vidyavahini/Banking_management_system && \
javac -cp "webapp/WEB-INF/lib/*" -d webapp/WEB-INF/classes src/com/banking/*/*.java && \
sudo cp -r webapp/WEB-INF/classes/* /var/lib/tomcat9/webapps/BankingApp/WEB-INF/classes/ && \
sudo systemctl restart tomcat9 && \
echo "✅ Deployed! Access at: http://localhost:8080/BankingApp/"
```

---

## 🌐 Application URLs

- **Home Page:** http://localhost:8080/BankingApp/
- **Create Account:** http://localhost:8080/BankingApp/createAccount.html
- **View Account:** http://localhost:8080/BankingApp/viewAccount.html
- **Deposit:** http://localhost:8080/BankingApp/deposit.html
- **Withdraw:** http://localhost:8080/BankingApp/withdraw.html
- **Transfer:** http://localhost:8080/BankingApp/transfer.html
- **Lifecycle Demo:** http://localhost:8080/BankingApp/AccountServlet

---

## 🛠️ Troubleshooting

### App Not Loading?
```bash
# Check if Tomcat is running
sudo systemctl status tomcat9

# Restart Tomcat
sudo systemctl restart tomcat9
```

### Import Errors in VS Code?
1. Press **Ctrl+Shift+P**
2. Type: `Java: Clean Java Language Server Workspace`
3. Select and restart

### Database Connection Issues?
```bash
# Check MySQL is running
sudo systemctl status mysql

# Start MySQL if needed
sudo systemctl start mysql
```

---

## 📚 Documentation

- **[SERVLET_CONCEPTS.md](SERVLET_CONCEPTS.md)** - Learn servlet concepts
- **[DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md)** - Detailed deployment steps
- **[SERVLET_README.md](SERVLET_README.md)** - Project overview

---

## 🎯 What's Fixed

✅ **Servlet API** - Added `javax.servlet-api-4.0.1.jar` to fix import errors  
✅ **Compilation** - All servlets compile successfully  
✅ **Deployment** - App deployed to Tomcat  
✅ **VS Code** - Settings updated to recognize servlet libraries  

---

**🎉 You're all set! Happy coding!**
