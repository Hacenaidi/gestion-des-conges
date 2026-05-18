package com.leavemng.dao;

import com.leavemng.models.LeaveBalance;
import com.leavemng.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.leavemng.dao.LeaveBalanceDAO; // Import LeaveBalanceDAO

public class LeaveBalanceDAO {

    public void checkLeaveBalance(int userId, int leaveTypeId, int days) {
        // Find the leave balance for the user and leave type
        LeaveBalance leaveBalance = findLeaveBalance(userId, leaveTypeId);

        if (leaveBalance != null) {
            // If there is a leave balance, check if it is enough for the requested days
            if (leaveBalance.getRemaining_days() >= days) {
                // If the leave balance is enough, update the consumed_days and remaining_days
                leaveBalance.setConsumed_days(leaveBalance.getConsumed_days() + days);
                leaveBalance.setRemaining_days(leaveBalance.getRemaining_days() - days);
                updateLeaveBalance(leaveBalance);
            } else {
                // If not enough, return an error message
                throw new IllegalArgumentException("Not enough leave balance");
            }
        } else {
            // If it does not exist, create a new leave balance for the user and leave type
            createLeaveBalance(userId, leaveTypeId);
            checkLeaveBalance(userId, leaveTypeId, days);
        }
    }

    private LeaveBalance createLeaveBalance(int userId, int leaveTypeId) {
        final int leaveTypeMaxDays = LeaveTypeDAO.getLeaveTypeById(leaveTypeId).getMax_days();
        String sql = "INSERT INTO leave_balance (uid, leave_type_id, consumed_days, remaining_days) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, leaveTypeId);
            stmt.setInt(3, 0);
            stmt.setInt(4, leaveTypeMaxDays);
            stmt.executeUpdate();
            return findLeaveBalance(userId, leaveTypeId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private LeaveBalance findLeaveBalance(int userId, int leaveTypeId) {
        String sql = "SELECT * FROM leave_balance WHERE uid = ? AND leave_type_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, leaveTypeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                LeaveBalance leaveBalance = new LeaveBalance();
                leaveBalance.setId(rs.getInt("id"));
                leaveBalance.setUid(rs.getInt("uid"));
                leaveBalance.setLeave_type_id(rs.getInt("leave_type_id"));
                leaveBalance.setConsumed_days(rs.getInt("consumed_days"));
                leaveBalance.setRemaining_days(rs.getInt("remaining_days"));
                return leaveBalance;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
    public void updateLeaveBalance(LeaveBalance leaveBalance) {
        String sql = "UPDATE leave_balance SET consumed_days = ?, remaining_days = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, leaveBalance.getConsumed_days());
            stmt.setInt(2, leaveBalance.getRemaining_days());
            stmt.setInt(3, leaveBalance.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
