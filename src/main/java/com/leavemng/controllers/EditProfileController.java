package com.leavemng.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.leavemng.models.User;
import com.leavemng.utils.Navigation;
import com.leavemng.utils.SessionManager;
import com.leavemng.dao.UserDAO;

public class EditProfileController {

    @FXML
    private TextField nameField;
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
        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) {
            errorLabel.setText("Your session has expired. Please log in again.");
            return;
        }
        nameField.setText(user.getUsername());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhone());
        departementField.setText(user.getDepartement());
        birthDateField.setText(user.getBirth_date());
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String departement = departementField.getText().trim();
        String birthDate = birthDateField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || departement.isEmpty() || birthDate.isEmpty()) {
            errorLabel.setText("All fields are required.");
            return;
        }

        try {
            User user = SessionManager.getInstance().getCurrentUser();
            if (user == null) {
                errorLabel.setText("Your session has expired. Please log in again.");
                return;
            }
            user.setUsername(name);
            user.setEmail(email);
            user.setPhone(phone);
            user.setDepartement(departement);
            user.setBirth_date(birthDate);

            UserDAO userDAO = new UserDAO();
            userDAO.updateUser(user);

            // Update session with new user data
            SessionManager.getInstance().setCurrentUser(user);

            Navigation.navigateToPage("/com/leavemng/views/Profile.fxml", event);
        } catch (Exception e) {
            errorLabel.setText("Unable to save your profile: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        Navigation.navigateToPage("/com/leavemng/views/Profile.fxml", event);
    }
}
