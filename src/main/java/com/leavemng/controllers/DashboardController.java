package com.leavemng.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import com.leavemng.models.User;
import com.leavemng.utils.SessionManager;
import com.leavemng.utils.UiTheme;
public class DashboardController {
    
    @FXML
    private Button requestsButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button requestLeaveButton;

    @FXML
    private Button adminDashboardButton;

    @FXML
    private Button logoutButton;

    @FXML
    private void handlePersonalRequestsButtonAction(ActionEvent event) {
        navigateToPage("/com/leavemng/views/PersonalRequests.fxml", event);
    }

    @FXML
    private void handleRequestLeaveButtonAction(ActionEvent event) {
        navigateToPage("/com/leavemng/views/RequestLeave.fxml", event);
    }

    @FXML
    private void handleProfileButtonAction(ActionEvent event) {
        navigateToPage("/com/leavemng/views/Profile.fxml", event);
    }

    @FXML
    private void handleAdminDashboardButtonAction(ActionEvent event) {
        navigateToPage("/com/leavemng/views/admin/Dashboard.fxml", event);
    }

    @FXML
    private void handleLogoutButtonAction(ActionEvent event) {
        SessionManager.getInstance().setCurrentUser(null);
        navigateToPage("/com/leavemng/views/Login.fxml", event);
    }
    public void initialize() {
        final User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            return;
        }

        final boolean isAdmin = currentUser.getIs_admin();
        adminDashboardButton.setVisible(isAdmin);
        adminDashboardButton.setManaged(isAdmin);

        if (isAdmin) {
            requestsButton.setVisible(false);
            requestsButton.setManaged(false);
            profileButton.setVisible(false);
            profileButton.setManaged(false);
            requestLeaveButton.setVisible(false);
            requestLeaveButton.setManaged(false);
        }
    }
    private void navigateToPage(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            UiTheme.apply(scene);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
