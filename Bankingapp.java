import java.util.*;
import java.sql.*;

// Custom Exception Classes
class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

class InvalidAmountException extends Exception {
    public InvalidAmountException(String message) {
        super(message);
    }
}

class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message) {
        super(message);
    }
}

class InvalidEmailException extends Exception {
    public InvalidEmailException(String message) {
        super(message);
    }
}

class DuplicateAccountException extends Exception {
    public DuplicateAccountException(String message) {
        super(message);
    }
}

interface LoanService {
    void applyLoan(double amount);
}

abstract class BankAccount {
    private int accountNumber;
    private String holdername;
    protected double balance;
    private String email;

    public BankAccount(int accountNumber, String holdername, double balance, String email)
            throws InvalidAmountException {
        if (balance < 0) {
            throw new InvalidAmountException("Initial balance cannot be negative");
        }
        this.accountNumber = accountNumber;
        this.holdername = holdername;
        this.balance = balance;
        this.email = email;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getHoldername() {
        return holdername;
    }

    public double getBalance() {
        return balance;
    }

    public String getEmail() {
        return email;
    }

    public void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive");
        }
        balance += amount;
        System.out.println("Amount Deposited: " + amount);
    }

    public void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive");
        }
        if (amount > balance) {
            throw new InsufficientBalanceException("Insufficient balance. Available: " + balance);
        }
        balance -= amount;
        System.out.println("Amount Withdrawn: " + amount);
    }

    public void showdetails() {
        System.out.println("Account Holder Name: " + holdername);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Email: " + email);
        System.out.println("Balance: " + balance);
    }

    abstract void CalculateInterest();
}

class SavingsAccount extends BankAccount {
    private double interestRate = 5.0;

    public SavingsAccount(int accNo, String name, double balance, String email) throws InvalidAmountException {
        super(accNo, name, balance, email);
    }

    @Override
    void CalculateInterest() {
        double interest = balance * interestRate / 100;
        System.out.println("Interest: " + interest);
    }
}

class CurrentAccount extends BankAccount {
    private double overdraftLimit = 10000;

    public CurrentAccount(int accNo, String name, double balance, String email) throws InvalidAmountException {
        super(accNo, name, balance, email);
    }

    @Override
    public void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive");
        }
        if (amount > balance + overdraftLimit) {
            throw new InsufficientBalanceException(
                    "Overdraft limit exceeded. Available: " + (balance + overdraftLimit));
        }
        balance -= amount;
        System.out.println("Amount Withdrawn: " + amount);
    }

    @Override
    void CalculateInterest() {
        System.out.println("No interest rate for Current Account");
    }
}

class BankingApp {
    static ArrayList<BankAccount> accounts = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    // Database connection details
    static final String DB_URL = "jdbc:mysql://localhost:3306/bankdb";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "root123";

    // Get database connection
    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Initialize database - creates database and table if they don't exist
    static void initializeDatabase() {
        // First, connect to MySQL server without specifying a database
        String serverUrl = "jdbc:mysql://localhost:3306/";

        try (Connection conn = DriverManager.getConnection(serverUrl, DB_USER, DB_PASSWORD);
                Statement stmt = conn.createStatement()) {

            // Create database if it doesn't exist
            String createDbSql = "CREATE DATABASE IF NOT EXISTS bankdb";
            stmt.executeUpdate(createDbSql);
            System.out.println("Database 'bankdb' is ready!");

            // Use the database
            stmt.executeUpdate("USE bankdb");

            // Create table if it doesn't exist
            String createTableSql = "CREATE TABLE IF NOT EXISTS accounts (" +
                    "account_number INT PRIMARY KEY, " +
                    "holder_name VARCHAR(100), " +
                    "balance DOUBLE, " +
                    "email VARCHAR(100), " +
                    "account_type VARCHAR(20))";
            stmt.executeUpdate(createTableSql);
            System.out.println("Table 'accounts' is ready!");
            System.out.println("Database initialized successfully!");

        } catch (SQLException e) {
            System.out.println("Database initialization failed: " + e.getMessage());
            System.out.println("Please check:");
            System.out.println("1. MySQL server is running");
            System.out.println("2. Username and password are correct");
            System.out.println("3. MySQL JDBC driver is in classpath");
        }
    }

    // Create table if not exists (kept for backward compatibility)
    static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS accounts (" +
                "account_number INT PRIMARY KEY, " +
                "holder_name VARCHAR(100), " +
                "balance DOUBLE, " +
                "email VARCHAR(100), " +
                "account_type VARCHAR(20))";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    // Load accounts from database
    static void loadAccountsFromDB() {
        String sql = "SELECT * FROM accounts";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int accNo = rs.getInt("account_number");
                String name = rs.getString("holder_name");
                double balance = rs.getDouble("balance");
                String email = rs.getString("email");
                String type = rs.getString("account_type");

                BankAccount account;
                if (type.equals("Savings")) {
                    account = new SavingsAccount(accNo, name, balance, email);
                } else {
                    account = new CurrentAccount(accNo, name, balance, email);
                }
                accounts.add(account);
            }
            System.out.println("Loaded " + accounts.size() + " accounts from database.");
        } catch (SQLException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        } catch (InvalidAmountException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Save account to database
    static void saveAccountToDB(BankAccount account, String accountType) {
        String sql = "INSERT INTO accounts (account_number, holder_name, balance, email, account_type) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, account.getAccountNumber());
            pstmt.setString(2, account.getHoldername());
            pstmt.setDouble(3, account.getBalance());
            pstmt.setString(4, account.getEmail());
            pstmt.setString(5, accountType);

            pstmt.executeUpdate();
            System.out.println("Account saved to database!");
        } catch (SQLException e) {
            System.out.println("Error saving account: " + e.getMessage());
        }
    }

    // Update account balance in database
    static void updateBalanceInDB(int accountNumber, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, accountNumber);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating balance: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Initialize database (creates database and table if they don't exist)
        initializeDatabase();
        loadAccountsFromDB();

        while (true) {
            try {
                System.out.println("\n====BANK MENU====");
                System.out.println("1. Create Account");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. Show Details");
                System.out.println("5. Apply Interest");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");

                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        deposit();
                        break;
                    case 3:
                        withdraw();
                        break;
                    case 4:
                        showAccount();
                        break;
                    case 5:
                        applyInterest();
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid input. Please enter a valid number.");
                sc.nextLine(); // Clear invalid input
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                sc.nextLine(); // Clear buffer
            }
        }
    }

    static void createAccount() {
        try {
            System.out.println("\n--- Create New Account ---");
            System.out.println("1. Savings Account");
            System.out.println("2. Current Account");
            System.out.print("Select account type: ");
            int type = sc.nextInt();
            sc.nextLine(); // Consume newline

            if (type != 1 && type != 2) {
                System.out.println("Invalid account type. Please select 1 or 2.");
                return;
            }

            System.out.println("Enter Account Number: ");
            int accNo = sc.nextInt();
            sc.nextLine(); // Consume newline

            // Check for duplicate account
            if (findaccount(accNo) != null) {
                throw new DuplicateAccountException("Account number " + accNo + " already exists!");
            }

            System.out.print("Enter Name: ");
            String name = sc.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Error: Name cannot be empty.");
                return;
            }

            System.out.print("Enter Email: ");
            String email = sc.nextLine().trim();

            // Validate email format
            if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                throw new InvalidEmailException("Invalid email format. Please use format: example@domain.com");
            }

            System.out.print("Enter Initial Balance: ");
            double balance = sc.nextDouble();
            sc.nextLine(); // Consume newline

            BankAccount newAccount;
            String accountType;
            if (type == 1) {
                newAccount = new SavingsAccount(accNo, name, balance, email);
                accountType = "Savings";
                System.out.println("Savings Account created successfully!");
            } else {
                newAccount = new CurrentAccount(accNo, name, balance, email);
                accountType = "Current";
                System.out.println("Current Account created successfully!");
            }
            accounts.add(newAccount);
            saveAccountToDB(newAccount, accountType);

        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input type. Please enter correct data types.");
            sc.nextLine(); // Clear invalid input
        } catch (InvalidAmountException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidEmailException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DuplicateAccountException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    static BankAccount findaccount(int accNo) throws AccountNotFoundException {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber() == accNo) {
                return account;
            }
        }
        return null;
    }

    static void deposit() {
        try {
            System.out.print("\nEnter Account Number: ");
            int accNo = sc.nextInt();
            sc.nextLine(); // Consume newline

            BankAccount account = findaccount(accNo);
            if (account == null) {
                throw new AccountNotFoundException("Account number " + accNo + " not found!");
            }

            System.out.print("Enter Amount to Deposit: ");
            double amount = sc.nextDouble();
            sc.nextLine(); // Consume newline

            account.deposit(amount);
            updateBalanceInDB(accNo, account.getBalance());
            System.out.println("Deposit successful!");
            System.out.println("New Balance: " + account.balance);

        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input. Please enter valid numbers.");
            sc.nextLine(); // Clear invalid input
        } catch (AccountNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidAmountException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    static void withdraw() {
        try {
            System.out.print("\nEnter Account Number: ");
            int accNo = sc.nextInt();
            sc.nextLine(); // Consume newline

            BankAccount account = findaccount(accNo);
            if (account == null) {
                throw new AccountNotFoundException("Account number " + accNo + " not found!");
            }

            System.out.print("Enter Amount to Withdraw: ");
            double amount = sc.nextDouble();
            sc.nextLine(); // Consume newline

            account.withdraw(amount);
            updateBalanceInDB(accNo, account.getBalance());
            System.out.println("Withdrawal successful!");
            System.out.println("Remaining Balance: " + account.balance);

        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input. Please enter valid numbers.");
            sc.nextLine(); // Clear invalid input
        } catch (AccountNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidAmountException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InsufficientBalanceException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    static void showAccount() {
        try {
            System.out.print("\nEnter Account Number: ");
            int accNo = sc.nextInt();
            sc.nextLine(); // Consume newline

            BankAccount account = findaccount(accNo);
            if (account == null) {
                throw new AccountNotFoundException("Account number " + accNo + " not found!");
            }

            System.out.println("\n--- Account Details ---");
            account.showdetails();

        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input. Please enter a valid account number.");
            sc.nextLine(); // Clear invalid input
        } catch (AccountNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    static void applyInterest() {
        try {
            System.out.print("\nEnter Account Number: ");
            int accNo = sc.nextInt();
            sc.nextLine(); // Consume newline

            BankAccount account = findaccount(accNo);
            if (account == null) {
                throw new AccountNotFoundException("Account number " + accNo + " not found!");
            }

            account.CalculateInterest();

        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input. Please enter a valid account number.");
            sc.nextLine(); // Clear invalid input
        } catch (AccountNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}