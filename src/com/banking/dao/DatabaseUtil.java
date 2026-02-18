package com.banking.dao;

import java.sql.*;

/**
 * DatabaseUtil - Centralized Database Connection Management
 * Handles database initialization and connection creation
 */
public class DatabaseUtil {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bankdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root123";

    // Static block to initialize database and table
    static {
        initializeDatabase();
    }

    /**
     * Initialize database - creates database and table if they don't exist
     * This runs automatically when the class is loaded
     */
    private static void initializeDatabase() {
        String serverUrl = "jdbc:mysql://localhost:3306/";

        try {
            // Load MySQL JDBC driver first
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[DatabaseUtil] MySQL JDBC Driver loaded successfully!");

            try (Connection conn = DriverManager.getConnection(serverUrl, DB_USER, DB_PASSWORD);
                    Statement stmt = conn.createStatement()) {

                // Create database if it doesn't exist
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS bankdb");
                System.out.println("[DatabaseUtil] Database 'bankdb' is ready!");

                // Use the database
                stmt.executeUpdate("USE bankdb");

                // Create accounts table
                String createAccountsTable = "CREATE TABLE IF NOT EXISTS accounts (" +
                        "account_number INT PRIMARY KEY, " +
                        "holder_name VARCHAR(100), " +
                        "balance DOUBLE, " +
                        "email VARCHAR(100), " +
                        "account_type VARCHAR(20))";
                stmt.executeUpdate(createAccountsTable);
                System.out.println("[DatabaseUtil] Table 'accounts' is ready!");

                // Create users table for customer authentication
                String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                        "user_id INT PRIMARY KEY AUTO_INCREMENT, " +
                        "username VARCHAR(50) UNIQUE, " +
                        "password VARCHAR(100), " +
                        "full_name VARCHAR(100), " +
                        "email VARCHAR(100), " +
                        "account_number INT, " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY (account_number) REFERENCES accounts(account_number))";
                stmt.executeUpdate(createUsersTable);
                System.out.println("[DatabaseUtil] Table 'users' is ready!");

                // Create admins table for bank staff authentication
                String createAdminsTable = "CREATE TABLE IF NOT EXISTS admins (" +
                        "admin_id INT PRIMARY KEY AUTO_INCREMENT, " +
                        "username VARCHAR(50) UNIQUE, " +
                        "password VARCHAR(100), " +
                        "full_name VARCHAR(100), " +
                        "email VARCHAR(100), " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
                stmt.executeUpdate(createAdminsTable);
                System.out.println("[DatabaseUtil] Table 'admins' is ready!");

                // Create default admin account if not exists
                String insertAdmin = "INSERT IGNORE INTO admins (username, password, full_name, email) " +
                        "VALUES ('admin', 'admin123', 'System Administrator', 'admin@bank.com')";
                stmt.executeUpdate(insertAdmin);
                System.out
                        .println("[DatabaseUtil] Default admin account created (username: admin, password: admin123)");

                System.out.println("[DatabaseUtil] ✅ Database initialized successfully on application startup!");

            }
        } catch (ClassNotFoundException e) {
            System.err.println("[DatabaseUtil] ❌ MySQL JDBC Driver not found!");
            System.err.println("[DatabaseUtil] Please ensure mysql-connector-j-8.0.33.jar is in WEB-INF/lib/");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[DatabaseUtil] ❌ Database initialization failed: " + e.getMessage());
            System.err.println("[DatabaseUtil] Please check:");
            System.err.println("  1. MySQL server is running");
            System.err.println("  2. Username and password are correct (root/root123)");
            e.printStackTrace();
        }
    }

    /**
     * Get a database connection
     * 
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Close database resources
     */
    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            System.err.println("[DatabaseUtil] Error closing resources: " + e.getMessage());
        }
    }
}
