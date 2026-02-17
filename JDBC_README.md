# Banking Application with JDBC

This is a simple banking application that demonstrates JDBC connectivity with MySQL database.

## Prerequisites

1. **Java Development Kit (JDK)** - Version 8 or higher
2. **MySQL Server** - Installed and running
3. **MySQL JDBC Driver** - Download `mysql-connector-java.jar`

## Setup Instructions

### Step 1: Install MySQL
Make sure MySQL is installed and running on your system.

### Step 2: Download JDBC Driver
Download MySQL Connector/J from: https://dev.mysql.com/downloads/connector/j/

### Step 3: Update Database Credentials
Open `Bankingapp.java` and update these lines with your MySQL credentials:
```java
static final String DB_URL = "jdbc:mysql://localhost:3306/bankdb";
static final String DB_USER = "root";
static final String DB_PASSWORD = "password";  // Change this to your MySQL password
```

**Note**: The database and table will be created automatically when you run the application!

### Step 4: Compile the Program
```bash
javac -cp .:mysql-connector-java.jar Bankingapp.java
```

### Step 5: Run the Program
```bash
java -cp .:mysql-connector-java.jar BankingApp
```

## Features

### JDBC Operations Implemented:

1. **CREATE** - Save new account to database
2. **READ** - Load all accounts from database on startup
3. **UPDATE** - Update account balance after deposit/withdrawal
4. **Database Connection** - Automatic connection management with try-with-resources

### How It Works:

- **On Startup**: The application automatically creates the database and table if they don't exist, then loads all existing accounts
- **Create Account**: New accounts are saved to both memory (ArrayList) and database
- **Deposit/Withdraw**: Balance updates are saved to the database immediately
- **Show Details**: Displays account information from memory (already loaded from DB)

## Database Schema

```sql
accounts (
    account_number INT PRIMARY KEY,
    holder_name VARCHAR(100),
    balance DOUBLE,
    email VARCHAR(100),
    account_type VARCHAR(20)
)
```

## Exception Handling

The application includes comprehensive exception handling for:
- Database connection errors
- SQL exceptions during CRUD operations
- Custom business logic exceptions (InsufficientBalance, InvalidAmount, etc.)
- Input validation errors

## Teaching Points

This application demonstrates:
1. **JDBC Basics**: Connection, Statement, PreparedStatement, ResultSet
2. **Database Operations**: INSERT, SELECT, UPDATE
3. **Resource Management**: try-with-resources for automatic connection closing
4. **Exception Handling**: Proper handling of SQLException
5. **Prepared Statements**: Protection against SQL injection
6. **Integration**: Combining OOP concepts with database persistence
