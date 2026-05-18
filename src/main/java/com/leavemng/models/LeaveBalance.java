package com.leavemng.models;

public class LeaveBalance {
    private int id;
    private int uid;
    private int leave_type_id;
    private int remaining_days;
    private int consumed_days;
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
    public int getLeave_type_id() {
        return leave_type_id;
    }
    public void setLeave_type_id(int leave_type_id) {
        this.leave_type_id = leave_type_id;
    }
    public int getRemaining_days() {
        return remaining_days;
    }
    public void setRemaining_days(int remaining_days) {
        this.remaining_days = remaining_days;
    }
    public int getConsumed_days() {
        return consumed_days;
    }
    public void setConsumed_days(int consumed_days) {
        this.consumed_days = consumed_days;
    }
    
}
