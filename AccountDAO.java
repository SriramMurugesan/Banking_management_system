import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    public static void createAccount(int accountNumber, String holderName, double balance, String email, String type)
            throws SQLException {
        String sql = "INSERT INTO accounts (account_number, holder_name, balance, email, account_type) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountNumber);
            pstmt.setString(2, holderName);
            pstmt.setDouble(3, balance);
            pstmt.setString(4, email);
            pstmt.setString(5, type);
            pstmt.executeUpdate();
        }
    }

    public static void updateBalance(int accountNumber, double amount, String transactionType) throws SQLException {
        String sqlSelect = "SELECT balance FROM accounts WHERE account_number = ?";
        String sqlUpdate = "UPDATE accounts SET balance = ? WHERE account_number = ?";

        try (Connection conn = DBConnection.getConnection()) {
            // Check current balance
            double currentBalance = 0;
            try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {
                pstmt.setInt(1, accountNumber);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        currentBalance = rs.getDouble("balance");
                    } else {
                        throw new SQLException("Account not found!");
                    }
                }
            }

            double newBalance = currentBalance;
            if ("DEPOSIT".equalsIgnoreCase(transactionType)) {
                newBalance += amount;
            } else if ("WITHDRAW".equalsIgnoreCase(transactionType)) {
                if (amount > currentBalance) {
                    throw new SQLException("Insufficient Balance!");
                }
                newBalance -= amount;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
                pstmt.setDouble(1, newBalance);
                pstmt.setInt(2, accountNumber);
                pstmt.executeUpdate();
            }
        }
    }

    public static String getAccountDetails(int accountNumber) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        StringBuilder details = new StringBuilder();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    details.append("Account Number: ").append(rs.getInt("account_number")).append("<br>");
                    details.append("Holder Name: ").append(rs.getString("holder_name")).append("<br>");
                    details.append("Balance: ").append(rs.getDouble("balance")).append("<br>");
                    details.append("Email: ").append(rs.getString("email")).append("<br>");
                    details.append("Type: ").append(rs.getString("account_type"));
                } else {
                    return "Account not found!";
                }
            }
        }
        return details.toString();
    }
}
