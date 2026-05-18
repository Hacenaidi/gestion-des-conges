package com.leavemng.models;

import java.time.LocalDate;

public class LeaveRequest {
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private int uid;
    private int id_type;
    private String status;
    
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public int getId_type() {
        return id_type;
    }
    public void setId_type(int id_type) {
        this.id_type = id_type;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getDays() {
        if (startDate != null && endDate != null) {
            return (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return "LeaveRequest{" +
                "id=" + id +
                ", uid=" + uid +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
    // Getters and setters
}
