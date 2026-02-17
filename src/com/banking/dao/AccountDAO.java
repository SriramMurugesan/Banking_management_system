package com.banking.dao;

import com.banking.model.Account;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AccountDAO - Data Access Object for Account operations
 * Handles all database CRUD operations for accounts
 */
public class AccountDAO {

    /**
     * Create a new account in the database
     */
    public boolean createAccount(Account account) {
        String sql = "INSERT INTO accounts (account_number, holder_name, balance, email, account_type) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, account.getAccountNumber());
            pstmt.setString(2, account.getHolderName());
            pstmt.setDouble(3, account.getBalance());
            pstmt.setString(4, account.getEmail());
            pstmt.setString(5, account.getAccountType());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("[AccountDAO] Error creating account: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get account by account number
     */
    public Account getAccount(int accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Account account = new Account();
                account.setAccountNumber(rs.getInt("account_number"));
                account.setHolderName(rs.getString("holder_name"));
                account.setBalance(rs.getDouble("balance"));
                account.setEmail(rs.getString("email"));
                account.setAccountType(rs.getString("account_type"));
                return account;
            }

        } catch (SQLException e) {
            System.err.println("[AccountDAO] Error getting account: " + e.getMessage());
        }

        return null;
    }

    /**
     * Update account balance
     */
    public boolean updateBalance(int accountNumber, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, accountNumber);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("[AccountDAO] Error updating balance: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all accounts
     */
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts";

        try (Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Account account = new Account();
                account.setAccountNumber(rs.getInt("account_number"));
                account.setHolderName(rs.getString("holder_name"));
                account.setBalance(rs.getDouble("balance"));
                account.setEmail(rs.getString("email"));
                account.setAccountType(rs.getString("account_type"));
                accounts.add(account);
            }

        } catch (SQLException e) {
            System.err.println("[AccountDAO] Error getting all accounts: " + e.getMessage());
        }

        return accounts;
    }

    /**
     * Check if account exists
     */
    public boolean accountExists(int accountNumber) {
        return getAccount(accountNumber) != null;
    }

    /**
     * Delete account
     */
    public boolean deleteAccount(int accountNumber) {
        String sql = "DELETE FROM accounts WHERE account_number = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, accountNumber);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("[AccountDAO] Error deleting account: " + e.getMessage());
            return false;
        }
    }
}
