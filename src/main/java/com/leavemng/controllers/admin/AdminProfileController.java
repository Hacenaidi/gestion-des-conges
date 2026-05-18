package com.leavemng.controllers.admin;

import com.leavemng.dao.UserDAO;
import com.leavemng.models.User;
import com.leavemng.utils.Navigation;
import com.leavemng.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.SQLException;

public class AdminProfileController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField departementField;

    @FXML
    private TextField birthDateField;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            errorLabel.setText("Your session has expired. Please log in again.");
            return;
        }

        // Load current user data
        usernameField.setText(currentUser.getUsername());
        emailField.setText(currentUser.getEmail());
        phoneField.setText(currentUser.getPhone());
        departementField.setText(currentUser.getDepartement());
        birthDateField.setText(currentUser.getBirth_date());
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String departement = departementField.getText().trim();
        String birthDate = birthDateField.getText().trim();

        // Validate inputs
        if (email.isEmpty() || phone.isEmpty() || departement.isEmpty() || birthDate.isEmpty()) {
            errorLabel.setText("All fields are required.");
            return;
        }

        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            errorLabel.setText("Your session has expired.");
            return;
        }

        // Check if new email is unique (if changed)
        if (!email.equals(currentUser.getEmail())) {
            try {
                UserDAO userDAO = new UserDAO();
                User existingUser = userDAO.getUserByEmail(email);
                if (existingUser != null) {
                    errorLabel.setText("This email is already in use.");
                    return;
                }
            } catch (SQLException e) {
                errorLabel.setText("Database error: " + e.getMessage());
                return;
            }
        }

        // Update user
        currentUser.setEmail(email);
        currentUser.setPhone(phone);
        currentUser.setDepartement(departement);
        currentUser.setBirth_date(birthDate);

        try {
            UserDAO userDAO = new UserDAO();
            userDAO.updateUser(currentUser);
            SessionManager.getInstance().setCurrentUser(currentUser);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Profile updated");
            alert.setContentText("Your profile has been updated successfully.");
            alert.showAndWait();

            // Go back to admin dashboard
            Navigation.navigateToPage("/com/leavemng/views/admin/Dashboard.fxml", event);
        } catch (SQLException e) {
            errorLabel.setText("Unable to update profile: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        Navigation.navigateToPage("/com/leavemng/views/admin/Dashboard.fxml", event);
    }
}
