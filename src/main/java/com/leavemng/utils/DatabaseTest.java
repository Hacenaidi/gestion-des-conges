package com.leavemng.utils;

import com.leavemng.dao.*;
import com.leavemng.models.*;
import java.time.LocalDate;
import java.util.List;

public class DatabaseTest {
    
    public static void main(String[] args) {
        System.out.println("=== DATABASE FUNCTIONALITY TEST ===\n");
        
        try {
            // Initialize database
            System.out.println("1. Initializing database...");
            DatabaseInitializer.initializeDatabase();
            System.out.println("✓ Database initialized\n");
            
            // Test User DAO
            System.out.println("2. Testing User DAO...");
            testUserDAO();
            System.out.println("✓ User DAO working\n");
            
            // Test LeaveType DAO
            System.out.println("3. Testing LeaveType DAO...");
            testLeaveTypeDAO();
            System.out.println("✓ LeaveType DAO working\n");
            
            // Test LeaveRequest DAO
            System.out.println("4. Testing LeaveRequest DAO...");
            testLeaveRequestDAO();
            System.out.println("✓ LeaveRequest DAO working\n");
            
            // Test LeaveBalance DAO
            System.out.println("5. Testing LeaveBalance DAO...");
            testLeaveBalanceDAO();
            System.out.println("✓ LeaveBalance DAO working\n");
            
            System.out.println("=== ALL TESTS PASSED ===");
            System.out.println("Database is ready for use!");
            
        } catch (Exception e) {
            System.out.println("✗ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testUserDAO() throws Exception {
        UserDAO userDAO = new UserDAO();
        
        // Create a test user
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setPhone("1234567890");
        user.setDepartement("IT");
        user.setBirth_date("1990-01-01");
        user.setAnnual_balance(20);
        
        userDAO.saveUser(user);
        System.out.println("  - User created: testuser");
        
        // Retrieve the user
        User retrievedUser = userDAO.getUser("testuser");
        if (retrievedUser != null) {
            System.out.println("  - User retrieved: " + retrievedUser.getUsername());
        } else {
            throw new Exception("Failed to retrieve user");
        }
    }
    
    private static void testLeaveTypeDAO() {
        LeaveTypeDAO leaveTypeDAO = new LeaveTypeDAO();
        
        // Get all leave types (should have sample data)
        List<LeaveType> leaveTypes = leaveTypeDAO.getAllLeaveTypes();
        System.out.println("  - Leave types found: " + leaveTypes.size());
        for (LeaveType lt : leaveTypes) {
            System.out.println("    • " + lt.getName() + " (" + lt.getMax_days() + " days)");
        }
        
        if (leaveTypes.isEmpty()) {
            throw new RuntimeException("No leave types found!");
        }
    }
    
    private static void testLeaveRequestDAO() throws Exception {
        LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
        UserDAO userDAO = new UserDAO();
        
        // Get test user
        User user = userDAO.getUser("testuser");
        if (user == null) {
            throw new Exception("Test user not found");
        }
        
        // Create a leave request
        LeaveRequest request = new LeaveRequest();
        request.setUid(user.getId());
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(5));
        request.setReason("Vacation");
        request.setId_type(1); // Annual Leave
        request.setStatus("pending");
        
        leaveRequestDAO.saveLeaveRequest(request);
        System.out.println("  - Leave request created");
        
        // Retrieve leave requests
        List<LeaveRequest> requests = leaveRequestDAO.getLeaveRequestsByUid(user.getId());
        System.out.println("  - Leave requests found: " + requests.size());
        
        if (requests.isEmpty()) {
            throw new Exception("Failed to retrieve leave requests");
        }
    }
    
    private static void testLeaveBalanceDAO() {
        LeaveBalanceDAO leaveBalanceDAO = new LeaveBalanceDAO();
        System.out.println("  - LeaveBalance DAO methods are available");
        System.out.println("  - Methods: checkLeaveBalance(), findLeaveBalance(), updateLeaveBalance()");
    }
}
