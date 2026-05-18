package com.leavemng.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import com.leavemng.models.User;
import com.leavemng.utils.Navigation;
import com.leavemng.utils.SessionManager;


public class ProfileController {

    @FXML
    private Label usernameLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label departementLabel;
    @FXML
    private Label birthDateLabel;
    @FXML
    private Label annualBalanceLabel;

    @FXML
    public void initialize() {
        User user = SessionManager.getInstance().getCurrentUser();
      if (user == null) {
        showLoginRequiredMessage();
        return;
      }
      nameLabel.setText(user.getUsername());
      emailLabel.setText(user.getEmail());
        phoneLabel.setText(user.getPhone());
        departementLabel.setText(user.getDepartement());
        birthDateLabel.setText(user.getBirth_date());
        annualBalanceLabel.setText(String.valueOf(user.getAnnual_balance()));
    }

    private void showLoginRequiredMessage() {
      Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Session expired");
      alert.setHeaderText(null);
        alert.setContentText("Your session has expired. Please log in again.");
      alert.showAndWait();
    }
    @FXML
    private void handleEditProfileButtonAction(ActionEvent event) {
      Navigation.navigateToPage("/com/leavemng/views/EditProfile.fxml", event);
    }

    @FXML
    private void handleGoBackButtonAction(ActionEvent event) {
      Navigation.navigateToPage("/com/leavemng/views/Dashboard.fxml", event);
    }

}