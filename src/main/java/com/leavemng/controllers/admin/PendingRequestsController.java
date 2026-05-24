package com.leavemng.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.leavemng.models.LeaveRequest;
import com.leavemng.utils.Navigation;
import com.leavemng.dao.LeaveRequestDAO;
import com.leavemng.dao.LeaveBalanceDAO;
import com.leavemng.dao.UserDAO;
import com.leavemng.models.User;
import com.leavemng.utils.EmailService;

import java.time.temporal.ChronoUnit;
import java.sql.SQLException;
import java.time.LocalDate;
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
    private Button approveButton;
    @FXML
    private Button rejectButton;
    @FXML
    private Button deleteButton;


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
            requestsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> updateActionButtons());
            updateActionButtons();
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
            updateActionButtons();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateActionButtons() {
        LeaveRequest selectedRequest = requestsTable.getSelectionModel().getSelectedItem();
        if (selectedRequest == null) {
            approveButton.setDisable(true);
            rejectButton.setDisable(true);
            deleteButton.setDisable(true);
            return;
        }

        LocalDate today = LocalDate.now();
        boolean endDateBeforeToday = selectedRequest.getEndDate() != null && selectedRequest.getEndDate().isBefore(today);
        boolean isPending = "pending".equalsIgnoreCase(selectedRequest.getStatus());
        boolean isApproved = "approved".equalsIgnoreCase(selectedRequest.getStatus());
        boolean isRejected = "rejected".equalsIgnoreCase(selectedRequest.getStatus());

        approveButton.setDisable(endDateBeforeToday || !isPending);
        rejectButton.setDisable(endDateBeforeToday || isRejected || (!isPending && !isApproved));
        deleteButton.setDisable(!(isRejected || (endDateBeforeToday && isApproved)));
    }

    @FXML
    public void handleDeleteButtonAction() {
        LeaveRequest leaveRequest = requestsTable.getSelectionModel().getSelectedItem();
        if (leaveRequest == null) {
            return;
        }

        LocalDate today = LocalDate.now();
        boolean endDateBeforeToday = leaveRequest.getEndDate() != null && leaveRequest.getEndDate().isBefore(today);
        boolean isApproved = "approved".equalsIgnoreCase(leaveRequest.getStatus());
        boolean isRejected = "rejected".equalsIgnoreCase(leaveRequest.getStatus());

        if (!(isRejected || (endDateBeforeToday && isApproved))) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete not allowed");
            alert.setHeaderText(null);
            alert.setContentText("You can delete rejected requests at any time, and approved requests only after the end date has passed.");
            alert.showAndWait();
            return;
        }

        LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
        try {
            leaveRequestDAO.deleteLeaveRequest(leaveRequest.getId());
            refreshRequests();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Request deleted");
            alert.setHeaderText(null);
            alert.setContentText("The leave request has been deleted successfully.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to delete the leave request.");
            alert.showAndWait();
        }
    }

    private void updateStatusAndNotify(LeaveRequest leaveRequest, String status) {
        LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
        UserDAO userDAO = new UserDAO();
        LeaveBalanceDAO leaveBalanceDAO = new LeaveBalanceDAO();

        try {
            User user = userDAO.getUserById(leaveRequest.getUid());
            if (user == null) {
                throw new SQLException("User not found.");
            }

            int requestedDays = leaveRequest.getDays();

            if ("approved".equals(status)) {
                if (!"approved".equalsIgnoreCase(leaveRequest.getStatus())) {
                    if (user.getAnnual_balance() < requestedDays) {
                        throw new IllegalArgumentException("Insufficient leave balance (" + user.getAnnual_balance() + " days).");
                    }

                    leaveBalanceDAO.consumeLeaveBalance(user.getId(), leaveRequest.getId_type(), requestedDays);
                    user.setAnnual_balance(user.getAnnual_balance() - requestedDays);
                    userDAO.updateAnnualBalance(user.getId(), user.getAnnual_balance());
                }

                leaveRequestDAO.approveLeaveRequest(leaveRequest.getId());
            } else if ("rejected".equals(status)) {
                if ("approved".equalsIgnoreCase(leaveRequest.getStatus())) {
                    int refundableDays = calculateRefundDays(leaveRequest);
                    if (refundableDays > 0) {
                        leaveBalanceDAO.refundLeaveBalance(user.getId(), leaveRequest.getId_type(), refundableDays);
                        user.setAnnual_balance(user.getAnnual_balance() + refundableDays);
                        userDAO.updateAnnualBalance(user.getId(), user.getAnnual_balance());
                    }
                }

                leaveRequestDAO.rejectLeaveRequest(leaveRequest.getId());
            }

            refreshRequests();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Leave request updated");
            alert.setHeaderText(null);
            alert.setContentText("The request was " + status + " successfully.");
            alert.showAndWait();

            sendLeaveDecisionEmailInBackground(user, leaveRequest, status);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
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

    private void sendLeaveDecisionEmailInBackground(User user, LeaveRequest leaveRequest, String status) {
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            return;
        }

        Thread emailThread = new Thread(() -> {
            try {
                EmailService.sendLeaveDecisionEmail(
                    user.getEmail(),
                    user.getUsername() == null ? "User" : user.getUsername(),
                    leaveRequest,
                    status
                );
            } catch (Exception emailException) {
                emailException.printStackTrace();
            }
        });
        emailThread.setDaemon(true);
        emailThread.start();
    }

    private int calculateRefundDays(LeaveRequest leaveRequest) {
        LocalDate startDate = leaveRequest.getStartDate();
        LocalDate endDate = leaveRequest.getEndDate();
        LocalDate today = LocalDate.now();

        if (startDate == null || endDate == null) {
            return 0;
        }

        if (today.isBefore(startDate)) {
            return leaveRequest.getDays();
        }

        if (today.isAfter(endDate)) {
            return 0;
        }

        return (int) ChronoUnit.DAYS.between(today, endDate);
    }
}