package org.example.individualproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    private static Connection connection;
    private static final String DB_URL = "jdbc:sqlite:IndividualProject.db";

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(DB_URL);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void initDatabase() {
        String usersTableSql = "CREATE TABLE IF NOT EXISTS users (" +
                               "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                               "username TEXT NOT NULL UNIQUE," +
                               "password TEXT NOT NULL," +
                               "name TEXT NOT NULL," +
                               "balance REAL NOT NULL DEFAULT 0," +
                               "secret_word TEXT NOT NULL" +
                               ");";
        String transactionsTableSql = "CREATE TABLE IF NOT EXISTS transactions (" +
                                      "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                      "user_id INTEGER NOT NULL," +
                                      "type TEXT NOT NULL," +
                                      "amount REAL NOT NULL," +
                                      "description TEXT," +
                                      "date TEXT NOT NULL," +
                                      "FOREIGN KEY (user_id) REFERENCES users(id)" +
                                      ");";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(usersTableSql);
            stmt.execute(transactionsTableSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean registerUser(String username, String password, String name, double initialBalance, String secretWord) {
        String sql = "INSERT INTO users(username, password, name, balance, secret_word) VALUES(?,?,?,?,?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, name);
            pstmt.setDouble(4, initialBalance);
            pstmt.setString(5, secretWord);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public User loginUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("name"), rs.getDouble("balance"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean addTransactionAndUpdateBalance(int userId, String type, double amount, String description, LocalDate date) {
        double currentBalance = getUserBalance(userId);
        double newBalance;
        if ("Витрата".equals(type)) {
            if (amount > currentBalance) return false;
            newBalance = currentBalance - amount;
        } else {
            newBalance = currentBalance + amount;
        }
        String transactionSql = "INSERT INTO transactions(user_id, type, amount, description, date) VALUES(?,?,?,?,?)";
        String updateUserSql = "UPDATE users SET balance = ? WHERE id = ?";
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(transactionSql)) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, type);
                pstmt.setDouble(3, amount);
                pstmt.setString(4, description);
                pstmt.setString(5, date.toString());
                pstmt.executeUpdate();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(updateUserSql)) {
                pstmt.setDouble(1, newBalance);
                pstmt.setInt(2, userId);
                pstmt.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try (Connection conn = getConnection()) {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            return false;
        }
    }
    
    public double getUserBalance(int userId) {
        String sql = "SELECT balance FROM users WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getDouble("balance");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0.0;
    }

    public List<Transaction> getTransactionsForUser(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE user_id = ? ORDER BY date DESC, id DESC";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                transactions.add(new Transaction(rs.getInt("id"), rs.getString("type"), rs.getDouble("amount"), rs.getString("description"), rs.getString("date")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return transactions;
    }

    public int verifyUserForDeletion(String username, String password, String secretWord) {
        String sql = "SELECT id FROM users WHERE username = ? AND password = ? AND secret_word = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, secretWord);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public boolean deleteUserAndTransactions(int userId) {
        String deleteTransactionsSql = "DELETE FROM transactions WHERE user_id = ?";
        String deleteUserSql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(deleteTransactionsSql)) {
                pstmt.setInt(1, userId);
                pstmt.executeUpdate();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(deleteUserSql)) {
                pstmt.setInt(1, userId);
                pstmt.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try (Connection conn = getConnection()) {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            return false;
        }
    }

    public boolean updateUserName(int userId, String newName) {
        String sql = "UPDATE users SET name = ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean updateUserPassword(int userId, String currentPassword, String newPassword) {
        String checkSql = "SELECT password FROM users WHERE id = ? AND password = ?";
        String updateSql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, currentPassword);
                ResultSet rs = pstmt.executeQuery();
                if (!rs.next()) return false;
            }
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setString(1, newPassword);
                pstmt.setInt(2, userId);
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Новий метод для отримання загального доходу за місяць
    public double getMonthlyIncome(int userId, LocalDate month) {
        String monthStart = month.withDayOfMonth(1).toString();
        String monthEnd = month.withDayOfMonth(month.lengthOfMonth()).toString();
        String sql = "SELECT SUM(amount) FROM transactions WHERE user_id = ? AND type = 'Дохід' AND date BETWEEN ? AND ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, monthStart);
            pstmt.setString(3, monthEnd);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0.0;
    }

    // Новий метод для отримання загальних витрат за місяць
    public double getMonthlyExpenses(int userId, LocalDate month) {
        String monthStart = month.withDayOfMonth(1).toString();
        String monthEnd = month.withDayOfMonth(month.lengthOfMonth()).toString();
        String sql = "SELECT SUM(amount) FROM transactions WHERE user_id = ? AND type = 'Витрата' AND date BETWEEN ? AND ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, monthStart);
            pstmt.setString(3, monthEnd);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0.0;
    }
}
