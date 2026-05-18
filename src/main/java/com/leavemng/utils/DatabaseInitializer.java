package com.leavemng.utils;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class DatabaseInitializer {
    
    public static void initializeDatabase() {
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create User table
                String createUserTable = "CREATE TABLE IF NOT EXISTS user (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL," +
                    "name TEXT," +
                    "email TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "phone TEXT," +
                    "is_admin BOOLEAN DEFAULT 0," +
                    "departement TEXT," +
                    "birth_date TEXT," +
                    "annual_balance INTEGER DEFAULT 30" +
                    ")";
            stmt.execute(createUserTable);
            System.out.println("✓ User table created/verified");
            
            // Create LeaveType table
            String createLeaveTypeTable = "CREATE TABLE IF NOT EXISTS leave_type (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "max_days INTEGER NOT NULL," +
                    "description TEXT" +
                    ")";
            stmt.execute(createLeaveTypeTable);
            System.out.println("✓ LeaveType table created/verified");
            
            // Create LeaveRequest table
                String createLeaveRequestTable = "CREATE TABLE IF NOT EXISTS leave_request (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "uid INTEGER NOT NULL," +
                    "start_date TEXT NOT NULL," +
                    "end_date TEXT NOT NULL," +
                    "reason TEXT," +
                    "id_type INTEGER NOT NULL," +
                    "status TEXT DEFAULT 'pending'," +
                    "days INTEGER DEFAULT 0," +
                    "FOREIGN KEY(uid) REFERENCES user(id)," +
                    "FOREIGN KEY(id_type) REFERENCES leave_type(id)" +
                    ")";
            stmt.execute(createLeaveRequestTable);
            System.out.println("✓ LeaveRequest table created/verified");
            
            // Create LeaveBalance table
            String createLeaveBalanceTable = "CREATE TABLE IF NOT EXISTS leave_balance (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "uid INTEGER NOT NULL," +
                    "leave_type_id INTEGER NOT NULL," +
                    "remaining_days INTEGER NOT NULL," +
                    "consumed_days INTEGER DEFAULT 0," +
                    "FOREIGN KEY(uid) REFERENCES user(id)," +
                    "FOREIGN KEY(leave_type_id) REFERENCES leave_type(id)" +
                    ")";
            stmt.execute(createLeaveBalanceTable);
            System.out.println("✓ LeaveBalance table created/verified");
            
            // Insert sample leave types if they don't exist
            String checkLeaveTypes = "SELECT COUNT(*) FROM leave_type";
            ResultSet rs = stmt.executeQuery(checkLeaveTypes);
            if (rs.next() && rs.getInt(1) == 0) {
                String[] insertLeaveTypes = {
                    "INSERT INTO leave_type (name, max_days, description) VALUES ('Annual Leave', 20, 'Annual vacation days')",
                    "INSERT INTO leave_type (name, max_days, description) VALUES ('Sick Leave', 10, 'For medical reasons')",
                    "INSERT INTO leave_type (name, max_days, description) VALUES ('Maternity Leave', 90, 'Maternity leave')",
                    "INSERT INTO leave_type (name, max_days, description) VALUES ('Paternity Leave', 14, 'Paternity leave')"
                };
                for (String sql : insertLeaveTypes) {
                    stmt.execute(sql);
                }
                System.out.println("✓ Sample leave types inserted");
            }
            
            System.out.println("✓ Database initialized successfully!");
            
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        initializeDatabase();
    }
}
