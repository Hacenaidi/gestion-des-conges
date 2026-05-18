package com.leavemng.controllers.admin;

import com.leavemng.models.LeaveType;

/**
 * Context class to pass LeaveType data between controllers
 */
public class EditLeaveTypeContext {
  private static EditLeaveTypeContext instance;
  private LeaveType leaveType;

  private EditLeaveTypeContext() {}

  public static EditLeaveTypeContext getInstance() {
    if (instance == null) {
      instance = new EditLeaveTypeContext();
    }
    return instance;
  }

  public LeaveType getLeaveType() {
    return leaveType;
  }

  public void setLeaveType(LeaveType leaveType) {
    this.leaveType = leaveType;
  }
}
