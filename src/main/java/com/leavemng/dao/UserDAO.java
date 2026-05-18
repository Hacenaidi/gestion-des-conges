package com.leavemng.dao;

import com.leavemng.models.User;
import com.leavemng.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public void saveUser(User user) throws SQLException {
        String sql = "INSERT INTO user (username, email, password, phone, departement, birth_date, annual_balance) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getDepartement());
            stmt.setString(6, user.getBirth_date());
            stmt.setInt(7, user.getAnnual_balance());
            stmt.executeUpdate();
        }
    }

    public User getUser(String username) throws SQLException {
        String sql = "SELECT * FROM user WHERE username = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                user.setIs_admin(rs.getBoolean("is_admin"));
                user.setDepartement (rs.getString("departement"));
                user.setBirth_date(rs.getString("birth_date"));
                user.setAnnual_balance(rs.getInt("annual_balance"));
                
                return user;
            }
        }
        return null;
    }

    public User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                user.setIs_admin(rs.getBoolean("is_admin"));
                user.setDepartement (rs.getString("departement"));
                user.setBirth_date(rs.getString("birth_date"));
                user.setAnnual_balance(rs.getInt("annual_balance"));
                return user;
            }
        }
        return null;
    }

    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                user.setIs_admin(rs.getBoolean("is_admin"));
                user.setDepartement (rs.getString("departement"));
                user.setBirth_date(rs.getString("birth_date"));
                user.setAnnual_balance(rs.getInt("annual_balance"));
                return user;
            }
        }
        return null;
    }

    public User updateUser(User user) throws SQLException {
        String sql = "UPDATE user SET username = ?, email = ?, password = ?, phone = ?, departement = ?, birth_date = ?, annual_balance = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getDepartement());
            stmt.setString(6, user.getBirth_date());
            stmt.setInt(7, user.getAnnual_balance());
            stmt.setInt(8, user.getId());
            stmt.executeUpdate();
        }
        return user;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user ORDER BY id ASC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                user.setIs_admin(rs.getBoolean("is_admin"));
                user.setDepartement(rs.getString("departement"));
                user.setBirth_date(rs.getString("birth_date"));
                user.setAnnual_balance(rs.getInt("annual_balance"));
                users.add(user);
            }
        }
        return users;
    }

    public List<User> getAllNonAdminUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user WHERE is_admin = 0 ORDER BY id ASC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                user.setIs_admin(rs.getBoolean("is_admin"));
                user.setDepartement(rs.getString("departement"));
                user.setBirth_date(rs.getString("birth_date"));
                user.setAnnual_balance(rs.getInt("annual_balance"));
                users.add(user);
            }
        }
        return users;
    }

    public void updateAnnualBalance(int userId, int annualBalance) throws SQLException {
        String sql = "UPDATE user SET annual_balance = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, annualBalance);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM user WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
}
