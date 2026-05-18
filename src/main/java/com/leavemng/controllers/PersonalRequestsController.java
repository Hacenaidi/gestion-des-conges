package com.leavemng.controllers;

import com.leavemng.dao.LeaveRequestDAO;
import com.leavemng.models.LeaveRequest;
import com.leavemng.utils.SessionManager;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.leavemng.utils.Navigation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PersonalRequestsController {

  @FXML
  private Button downloadButton;

  @FXML
  private TableView<LeaveRequest> requestsTable;

  // handleDashboardButtonAction

  @FXML
  public void handleDashboardButtonAction(ActionEvent event) {
    Navigation.navigateToPage("/com/leavemng/views/Dashboard.fxml", event);
  }


  
  @FXML
  public void handleDownloadButtonAction() {
    try (
      BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))
    ) {
      for (LeaveRequest request : requestsTable.getItems()) {
        writer.write(request.toString());
        writer.newLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void initialize() {
    LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
    if (SessionManager.getInstance().getCurrentUser() == null) {
      requestsTable.setPlaceholder(new Label("Your session has expired. Please log in again."));
      return;
    }
    int uid = SessionManager.getInstance().getCurrentUser().getId();
    try {
      List<LeaveRequest> leaveRequests = leaveRequestDAO.getLeaveRequestsByUid(
        uid
      );
      System.out.println(
        "Leave Requests for user with id " + uid + ": " + leaveRequests.size()
      );
      ObservableList<LeaveRequest> observableList = FXCollections.observableArrayList(
        leaveRequests
      );

      TableColumn<LeaveRequest, Integer> idColumn = new TableColumn<>("ID");
      idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

      // Do not show User ID column for personal requests (current user)

      TableColumn<LeaveRequest, LocalDate> startDateColumn = new TableColumn<>(
        "Start Date"
      );
      startDateColumn.setCellValueFactory(
        new PropertyValueFactory<>("startDate")
      );

      TableColumn<LeaveRequest, LocalDate> endDateColumn = new TableColumn<>(
        "End Date"
      );
      endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

      TableColumn<LeaveRequest, Integer> daysColumn = new TableColumn<>(
        "Days"
      );
      daysColumn.setCellValueFactory(new PropertyValueFactory<>("days"));

      TableColumn<LeaveRequest, String> reasonColumn = new TableColumn<>(
        "Reason"
      );
      reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));

      TableColumn<LeaveRequest, String> statusColumn = new TableColumn<>(
        "Status"
      );
      statusColumn.setCellValueFactory(new PropertyValueFactory<>("status")); // Corrected line

      requestsTable
        .getColumns()
        .addAll(
          idColumn,
          startDateColumn,
          endDateColumn,
          daysColumn,
          reasonColumn,
          statusColumn
        );
      requestsTable.setItems(observableList);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
