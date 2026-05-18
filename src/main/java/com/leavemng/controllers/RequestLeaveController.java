package com.leavemng.controllers;

import com.leavemng.dao.LeaveRequestDAO;
import com.leavemng.dao.LeaveTypeDAO; // Import LeaveTypeDAO
import com.leavemng.models.LeaveRequest;
import com.leavemng.models.LeaveType; // Import LeaveType
import com.leavemng.utils.Navigation;
import com.leavemng.utils.SessionManager;
import java.time.LocalDate;
import java.util.List; // Import List
import javafx.collections.FXCollections; // Import FXCollections
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox; // Import ComboBox
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import com.leavemng.models.User; // Import User
import com.leavemng.dao.LeaveBalanceDAO; // Import LeaveBalanceDAO
import com.leavemng.dao.UserDAO; // Import UserDAO
public class RequestLeaveController {

  @FXML
  private DatePicker startDatePicker;

  @FXML
  private DatePicker endDatePicker;

  @FXML
  private TextArea reasonTextArea;

  @FXML
  private Label errorLabel;

  @FXML
  private ComboBox<LeaveType> idTypeComboBox; // Change TextField to ComboBox

  @FXML
  private Label daysLabel;

  private void updateDaysDisplay() {
    LocalDate startDate = startDatePicker.getValue();
    LocalDate endDate = endDatePicker.getValue();
    
    if (startDate != null && endDate != null) {
      long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
      if (days <= 0) {
        daysLabel.setText("Invalid date range. The end date must be after the start date.");
        daysLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ef4444;");
      } else {
        daysLabel.setText(days + " day(s)");
        daysLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #3b82f6;");
      }
    } else {
      daysLabel.setText("Please select both dates.");
      daysLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #9ca3af;");
    }
  }

  @FXML
  public void handleDashboardButtonAction(ActionEvent event) {
    Navigation.navigateToPage("/com/leavemng/views/Dashboard.fxml", event);
  }

  @FXML
  public void initialize() {
    LeaveTypeDAO leaveTypeDAO = new LeaveTypeDAO();
    List<LeaveType> leaveTypes = leaveTypeDAO.getAllLeaveTypes(); // Get all leave types
    System.out.println("--------------------------");
    System.out.println(leaveTypes);
    idTypeComboBox.setItems(FXCollections.observableArrayList(leaveTypes)); // Set items in ComboBox
    
    // Add listeners to DatePickers to update days display
    startDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> updateDaysDisplay());
    endDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> updateDaysDisplay());
  }



  @FXML
  private void handleSubmitButtonAction(ActionEvent event) {
    final User currentUser = SessionManager.getInstance().getCurrentUser();
    if (currentUser == null) {
      errorLabel.setText("Your session has expired. Please log in again.");
      return;
    }

    LocalDate startDate = startDatePicker.getValue();
    LocalDate endDate = endDatePicker.getValue();
    String reason = reasonTextArea.getText();
    LeaveType selectedLeaveType = idTypeComboBox
      .getSelectionModel()
      .getSelectedItem(); // Get selected leave type

    if (
      startDate == null ||
      endDate == null ||
      reason.isEmpty() ||
      selectedLeaveType == null
    ) {
      errorLabel.setText("Please fill in all fields.");
      return;
    }

    int idType = selectedLeaveType.getId(); // Get id from selected leave type

    LeaveRequest leaveRequest = new LeaveRequest();
  leaveRequest.setUid(currentUser.getId());
    leaveRequest.setStartDate(startDate);
    leaveRequest.setEndDate(endDate);
    leaveRequest.setReason(reason);
    leaveRequest.setId_type(idType); // Set id_type
    

    // Difference between start date and end date.
    final int days = (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);

    if (days <= 0) {
      errorLabel.setText("Invalid date range. The end date must be after the start date.");
        return;
    }
    if (days>currentUser.getAnnual_balance()) {
      errorLabel.setText("Insufficient leave balance ("+currentUser.getAnnual_balance()+" days).");
        return;
    }
    
    // checkLeaveBalance
    LeaveBalanceDAO leaveBalanceDAO = new LeaveBalanceDAO();
    try {
      leaveBalanceDAO.checkLeaveBalance(currentUser.getId(), idType, days);
      currentUser.setAnnual_balance(currentUser.getAnnual_balance()-days);

        UserDAO userDAO = new UserDAO();
        userDAO.updateAnnualBalance(currentUser.getId(), currentUser.getAnnual_balance());
    } catch (Exception e) { 
    //   show error message
      errorLabel.setText(e.getMessage());
      return;
    }


    LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
    try {
      leaveRequestDAO.saveLeaveRequest(leaveRequest);
      errorLabel.setText("Leave request submitted successfully!");
      Navigation.navigateToPage("/com/leavemng/views/PersonalRequests.fxml", event);
    } catch (Exception e) {
      e.printStackTrace();
      errorLabel.setText("Something went wrong. Please try again.");
    }
  }
}
