package com.leavemng.models;

public class LeaveType {
    private int id;
    private String name;
    private int max_days;
    private String description;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getMax_days() {
        return max_days;
    }
    public void setMax_days(int max_days) {
        this.max_days = max_days;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return this.name; // this will be displayed in the ComboBox
    }

    }
