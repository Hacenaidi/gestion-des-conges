package com.leavemng.controllers.admin;

import com.leavemng.dao.LeaveTypeDAO;
import com.leavemng.models.LeaveType;
import com.leavemng.utils.Navigation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;

public class LeaveTypeController {




  @FXML
  private TextField nameField;

  @FXML
  private TextField maxDaysField;
  
  @FXML
  private TextField descriptionField;
  
    @FXML
    private Label error;
  
  @FXML
  private void handleAddLeaveTypeButtonAction(ActionEvent event) {
    Navigation.navigateToPage(
      "/com/leavemng/views/admin/AddLeaveType.fxml",
      event
    );
  }

  @FXML
  private void handleDashboardButtonAction(ActionEvent event) {
    Navigation.navigateToPage("/com/leavemng/views/admin/Dashboard.fxml", event);
  }

  @FXML
  private void handleAddButtonAction(ActionEvent event) {
      String name = nameField.getText();
      int maxDays = Integer.parseInt(maxDaysField.getText());
      String description = descriptionField.getText();
  
      LeaveType newLeaveType = new LeaveType();
      newLeaveType.setName(name);
      newLeaveType.setMax_days(maxDays);
      newLeaveType.setDescription(description);


      // controlle de saisie
      
    
      LeaveTypeDAO leaveTypeDAO = new LeaveTypeDAO();
      leaveTypeDAO.addLeaveType(newLeaveType);
      Navigation.navigateToPage(
        "/com/leavemng/views/admin/LeaveType.fxml",
        event
      );

    }

  @FXML
  private void handleEditLeaveTypeButtonAction(ActionEvent event) {
    // Code to handle editing a leave type
  }

  @FXML
  private void handleDeleteLeaveTypeButtonAction(ActionEvent event) {
    // Code to handle deleting a leave type
  }


}
