package com.leavemng.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateUserRole {
    
    public static void setUserAsAdmin(String username) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE user SET is_admin = 1 WHERE username = ?")) {
            stmt.setString(1, username);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✓ User '" + username + "' is now an ADMIN");
            } else {
                System.out.println("✗ User '" + username + "' not found");
            }
        } catch (SQLException e) {
            System.out.println("Error updating user role: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void removeAdminRole(String username) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE user SET is_admin = 0 WHERE username = ?")) {
            stmt.setString(1, username);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✓ User '" + username + "' is no longer an ADMIN");
            } else {
                System.out.println("✗ User '" + username + "' not found");
            }
        } catch (SQLException e) {
            System.out.println("Error updating user role: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java UpdateUserRole <command> <username>");
            System.out.println("Commands:");
            System.out.println("  grant <username>  - Make user an admin");
            System.out.println("  revoke <username> - Remove admin role");
            System.out.println("\nExample: java UpdateUserRole grant john");
            return;
        }
        
        String command = args[0].toLowerCase();
        String username = "hacen";
        
        if (command.equals("grant")) {
            setUserAsAdmin(username);
        } else if (command.equals("revoke")) {
            removeAdminRole(username);
        } else {
            System.out.println("Unknown command: " + command);
        }
    }
}
