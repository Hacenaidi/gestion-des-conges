package com.leavemng.controllers.admin;

import com.leavemng.dao.LeaveTypeDAO;
import com.leavemng.models.LeaveType;
import com.leavemng.utils.Navigation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditLeaveTypeController {

  @FXML
  private Label pageTitle;

  @FXML
  private TextField nameField;

  @FXML
  private TextField maxDaysField;
  
  @FXML
  private TextField descriptionField;
  
  @FXML
  private Label error;

  private LeaveType currentLeaveType;
  private boolean isEditMode = false;

  @FXML
  public void initialize() {
    // Check if we're in edit mode
    LeaveType leaveType = EditLeaveTypeContext.getInstance().getLeaveType();
    if (leaveType != null) {
      isEditMode = true;
      currentLeaveType = leaveType;
      pageTitle.setText("Edit Leave Type");
      nameField.setText(leaveType.getName());
      maxDaysField.setText(String.valueOf(leaveType.getMax_days()));
      descriptionField.setText(leaveType.getDescription());
    } else {
      pageTitle.setText("Add Leave Type");
    }
  }

  @FXML
  private void handleSaveButtonAction(ActionEvent event) {
    String name = nameField.getText().trim();
    String maxDaysStr = maxDaysField.getText().trim();
    String description = descriptionField.getText().trim();

    if (name.isEmpty() || maxDaysStr.isEmpty() || description.isEmpty()) {
      error.setText("All fields are required.");
      return;
    }

    try {
      int maxDays = Integer.parseInt(maxDaysStr);

      if (isEditMode) {
        // Update mode
        currentLeaveType.setName(name);
        currentLeaveType.setMax_days(maxDays);
        currentLeaveType.setDescription(description);
        
        LeaveTypeDAO leaveTypeDAO = new LeaveTypeDAO();
        leaveTypeDAO.updateLeaveType(currentLeaveType);
        error.setText("Leave type updated successfully.");
      } else {
        // Add mode
        LeaveType newLeaveType = new LeaveType();
        newLeaveType.setName(name);
        newLeaveType.setMax_days(maxDays);
        newLeaveType.setDescription(description);

        LeaveTypeDAO leaveTypeDAO = new LeaveTypeDAO();
        leaveTypeDAO.addLeaveType(newLeaveType);
        error.setText("Leave type added successfully.");
      }

      // Clear context and navigate back after a short delay
      EditLeaveTypeContext.getInstance().setLeaveType(null);
      Navigation.navigateToPage(
        "/com/leavemng/views/admin/LeaveType.fxml",
        event
      );
    } catch (NumberFormatException e) {
      error.setText("Max days must be a valid number.");
    }
  }

  @FXML
  private void handleBackButtonAction(ActionEvent event) {
    EditLeaveTypeContext.getInstance().setLeaveType(null);
    Navigation.navigateToPage("/com/leavemng/views/admin/LeaveType.fxml", event);
  }
}
