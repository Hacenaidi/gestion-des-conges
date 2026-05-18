package com.leavemng;

import java.sql.Connection;
import com.leavemng.utils.DatabaseUtil;

public class TestConnection {
    public static void main(String[] args) {
        try {
            System.out.println("Testing database connection...");
            Connection conn = DatabaseUtil.getConnection();
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ SUCCESS: Database connection established!");
                System.out.println("✓ Connection URL: jdbc:mysql://localhost:3306/leavemng");
                System.out.println("✓ Connected as: root");
                conn.close();
                System.out.println("✓ Connection closed successfully.");
            } else {
                System.out.println("✗ FAILED: Connection is null or closed.");
            }
        } catch (Exception e) {
            System.out.println("✗ FAILED: Connection error");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
