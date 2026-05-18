package com.leavemng.dao;

import com.leavemng.models.LeaveType;
import com.leavemng.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LeaveTypeDAO {
    public void addLeaveType(LeaveType leaveType) {
        String sql = "INSERT INTO leave_type (name, max_days, description) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, leaveType.getName());
            stmt.setInt(2, leaveType.getMax_days());
            stmt.setString(3, leaveType.getDescription());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateLeaveType(LeaveType leaveType) {
        String sql = "UPDATE leave_type SET name = ?, max_days = ?, description = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, leaveType.getName());
            stmt.setInt(2, leaveType.getMax_days());
            stmt.setString(3, leaveType.getDescription());
            stmt.setInt(4, leaveType.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteLeaveType(LeaveType leaveType) {
        String sql = "DELETE FROM leave_type WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, leaveType.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static LeaveType getLeaveTypeById(int id) {
        LeaveType leaveType = new LeaveType();
        String sql = "SELECT * FROM leave_type WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                leaveType.setId(rs.getInt("id"));
                leaveType.setName(rs.getString("name"));
                leaveType.setMax_days(rs.getInt("max_days"));
                leaveType.setDescription(rs.getString("description"));
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return leaveType;
    }

    public List<LeaveType> getAllLeaveTypes() {
        List<LeaveType> leaveTypes = new ArrayList<>();
        String sql = "SELECT * FROM leave_type";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LeaveType leaveType = new LeaveType();
                leaveType.setId(rs.getInt("id"));
                leaveType.setName(rs.getString("name"));
                leaveType.setMax_days(rs.getInt("max_days"));
                leaveType.setDescription(rs.getString("description"));
                leaveTypes.add(leaveType);
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return leaveTypes;
    }
}