package com.leavemng.dao;

import com.leavemng.models.LeaveRequest;
import com.leavemng.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class LeaveRequestDAO {
    public void saveLeaveRequest(LeaveRequest leaveRequest) throws SQLException {
        String sql = "INSERT INTO leave_request (uid, start_date, end_date, reason, id_type) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, leaveRequest.getUid());
            stmt.setString(2, leaveRequest.getStartDate().toString());
            stmt.setString(3, leaveRequest.getEndDate().toString());
            stmt.setString(4, leaveRequest.getReason());
            stmt.setInt(5, leaveRequest.getId_type());
            stmt.executeUpdate();
        }
    }

    public List<LeaveRequest> getLeaveRequestsByUid(int uid) throws SQLException {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        String sql = "SELECT * FROM leave_request WHERE uid = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, uid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LeaveRequest leaveRequest = new LeaveRequest();
                leaveRequest.setId(rs.getInt("id"));
                leaveRequest.setUid(rs.getInt("uid"));
                leaveRequest.setStartDate(readLocalDate(rs.getString("start_date")));
                leaveRequest.setEndDate(readLocalDate(rs.getString("end_date")));
                leaveRequest.setReason(rs.getString("reason"));
                leaveRequest.setId_type(rs.getInt("id_type"));
                leaveRequest.setStatus(rs.getString("status"));
                leaveRequests.add(leaveRequest);
            }
        }
        return leaveRequests;
    }
    public LeaveRequest updateLeaveRequest(LeaveRequest leaveRequest) throws SQLException {
        String sql = "UPDATE leave_request SET start_date = ?, end_date = ?, reason = ?, id_type = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, leaveRequest.getStartDate().toString());
            stmt.setString(2, leaveRequest.getEndDate().toString());
            stmt.setString(3, leaveRequest.getReason());
            stmt.setInt(4, leaveRequest.getId_type());
            stmt.setInt(5, leaveRequest.getId());
            stmt.executeUpdate();
        }
        return leaveRequest;
    }       

    public void approveLeaveRequest(int id) throws SQLException {
        String sql = "UPDATE leave_request SET status = 'approved' WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    public void rejectLeaveRequest(int id) throws SQLException {
        String sql = "UPDATE leave_request SET status = 'rejected' WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<LeaveRequest> getAllLeaveRequestsOrdered() throws SQLException {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        String sql = "SELECT * FROM leave_request ORDER BY CASE status WHEN 'pending' THEN 0 WHEN 'approved' THEN 1 WHEN 'rejected' THEN 2 ELSE 3 END, id ASC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                LeaveRequest leaveRequest = new LeaveRequest();
                leaveRequest.setId(rs.getInt("id"));
                leaveRequest.setUid(rs.getInt("uid"));
                leaveRequest.setStartDate(readLocalDate(rs.getString("start_date")));
                leaveRequest.setEndDate(readLocalDate(rs.getString("end_date")));
                leaveRequest.setReason(rs.getString("reason"));
                leaveRequest.setId_type(rs.getInt("id_type"));
                leaveRequest.setStatus(rs.getString("status"));
                leaveRequests.add(leaveRequest);
            }
        }
        return leaveRequests;
    }


    public List<LeaveRequest> getPendingLeaveRequests() throws SQLException {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        String sql = "SELECT * FROM leave_request WHERE status = 'pending'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LeaveRequest leaveRequest = new LeaveRequest();
                leaveRequest.setId(rs.getInt("id"));
                leaveRequest.setUid(rs.getInt("uid"));
                leaveRequest.setStartDate(readLocalDate(rs.getString("start_date")));
                leaveRequest.setEndDate(readLocalDate(rs.getString("end_date")));
                leaveRequest.setReason(rs.getString("reason"));
                leaveRequest.setId_type(rs.getInt("id_type"));
                leaveRequest.setStatus(rs.getString("status"));
                leaveRequests.add(leaveRequest);
            }
        }
        return leaveRequests;
    }

    private LocalDate readLocalDate(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return null;
        }

        if (rawValue.matches("\\d+")) {
            long epochMillis = Long.parseLong(rawValue);
            return Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()).toLocalDate();
        }

        return LocalDate.parse(rawValue);
    }
}