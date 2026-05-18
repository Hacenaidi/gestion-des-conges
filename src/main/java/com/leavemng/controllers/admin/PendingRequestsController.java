package com.leavemng.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.leavemng.models.LeaveRequest;
import com.leavemng.utils.Navigation;
import com.leavemng.dao.LeaveRequestDAO;
import com.leavemng.dao.UserDAO;
import com.leavemng.models.User;
import com.leavemng.utils.EmailService;

import java.sql.SQLException;
import java.util.List;

public class PendingRequestsController {
    @FXML
    private TableView<LeaveRequest> requestsTable;
    @FXML
    private TableColumn<LeaveRequest, Integer> idColumn;
    @FXML
    private TableColumn<LeaveRequest, String> userColumn;
    @FXML
    private TableColumn<LeaveRequest, Integer> daysColumn;
    @FXML
    private TableColumn<LeaveRequest, String> startDateColumn;
    @FXML
    private TableColumn<LeaveRequest, String> endDateColumn;
    @FXML
    private TableColumn<LeaveRequest, String> reasonColumn;
    @FXML
    private TableColumn<LeaveRequest, String> statusColumn;


    @FXML
    public void handleApproveButtonAction() {
        LeaveRequest leaveRequest = requestsTable.getSelectionModel().getSelectedItem();
        if (leaveRequest != null) {
            updateStatusAndNotify(leaveRequest, "approved");
        }
    }

    @FXML
    public void handleRejectButtonAction() {
        LeaveRequest leaveRequest = requestsTable.getSelectionModel().getSelectedItem();
        if (leaveRequest != null) {
            updateStatusAndNotify(leaveRequest, "rejected");
        }
    }

    @FXML
    private void handleDashboardButtonAction(ActionEvent event) {
        Navigation.navigateToPage("/com/leavemng/views/admin/Dashboard.fxml", event);
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        // userColumn will display the user's full name fetched from UserDAO
        userColumn.setCellValueFactory(cellData -> {
            LeaveRequest lr = cellData.getValue();
            try {
                UserDAO userDAO = new UserDAO();
                com.leavemng.models.User u = userDAO.getUserById(lr.getUid());
                String name = u != null ? (u.getUsername() != null ? u.getUsername() : "Unknown") : "Unknown";
                return new javafx.beans.property.SimpleStringProperty(name);
            } catch (Exception e) {
                e.printStackTrace();
                return new javafx.beans.property.SimpleStringProperty("Unknown");
            }
        });
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        daysColumn.setCellValueFactory(cellData -> {
            LeaveRequest lr = cellData.getValue();
            if (lr.getStartDate() == null || lr.getEndDate() == null) {
                return new javafx.beans.property.SimpleObjectProperty<>(0);
            }
            long days = java.time.temporal.ChronoUnit.DAYS.between(lr.getStartDate(), lr.getEndDate()) + 1;
            return new javafx.beans.property.SimpleObjectProperty<>((int) days);
        });

        LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
        try {
            List<LeaveRequest> leaveRequests = leaveRequestDAO.getAllLeaveRequestsOrdered();
            ObservableList<LeaveRequest> observableList = FXCollections.observableArrayList(leaveRequests);
            requestsTable.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshRequests() {
        LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
        try {
            List<LeaveRequest> leaveRequests = leaveRequestDAO.getAllLeaveRequestsOrdered();
            ObservableList<LeaveRequest> observableList = FXCollections.observableArrayList(leaveRequests);
            requestsTable.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateStatusAndNotify(LeaveRequest leaveRequest, String status) {
        LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
        UserDAO userDAO = new UserDAO();

        try {
            if ("approved".equals(status)) {
                leaveRequestDAO.approveLeaveRequest(leaveRequest.getId());
            } else if ("rejected".equals(status)) {
                leaveRequestDAO.rejectLeaveRequest(leaveRequest.getId());
            }

            User user = userDAO.getUserById(leaveRequest.getUid());
            boolean emailSent = false;

            if (user != null && user.getEmail() != null && !user.getEmail().isBlank()) {
                try {
                    EmailService.sendLeaveDecisionEmail(
                        user.getEmail(),
                        user.getUsername() == null ? "User" : user.getUsername(),
                        leaveRequest,
                        status
                    );
                    emailSent = true;
                } catch (Exception emailException) {
                    emailException.printStackTrace();
                    Alert emailAlert = new Alert(Alert.AlertType.WARNING);
                    emailAlert.setTitle("Email not sent");
                    emailAlert.setHeaderText(null);
                    emailAlert.setContentText(
                        "The request was " + status + ", but the email failed: " + emailException.getMessage()
                    );
                    emailAlert.showAndWait();
                }
            }

            refreshRequests();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Leave request updated");
            alert.setHeaderText(null);
            if (emailSent) {
                alert.setContentText("The request was " + status + " and an email was sent to the user.");
            } else {
                alert.setContentText("The request was " + status + ", but the email could not be sent.");
            }
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to update the leave request.");
            alert.showAndWait();
        }
    }
}