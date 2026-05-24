package com.leavemng.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.leavemng.models.User;
import com.leavemng.utils.Navigation;
import com.leavemng.utils.SessionManager;
import com.leavemng.dao.UserDAO;
import com.leavemng.utils.PasswordUtil;
import java.sql.SQLException;

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
    private PasswordField currentPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
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
        String currentPassword = currentPasswordField.getText().trim();
        String newPassword = newPasswordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || departement.isEmpty() || birthDate.isEmpty()) {
            errorLabel.setText("All fields are required.");
            return;
        }

        boolean wantsPasswordChange = !currentPassword.isEmpty() || !newPassword.isEmpty() || !confirmPassword.isEmpty();
        if (wantsPasswordChange && (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty())) {
            errorLabel.setText("Please fill in all password fields to change your password.");
            return;
        }

        try {
            User user = SessionManager.getInstance().getCurrentUser();
            if (user == null) {
                errorLabel.setText("Your session has expired. Please log in again.");
                return;
            }

            UserDAO userDAO = new UserDAO();

            if (!name.equals(user.getUsername())) {
                User existingUsername = userDAO.getUser(name);
                if (existingUsername != null && existingUsername.getId() != user.getId()) {
                    errorLabel.setText("This username is already taken.");
                    return;
                }
            }

            if (!email.equals(user.getEmail())) {
                User existingEmail = userDAO.getUserByEmailIgnoreCase(email);
                if (existingEmail != null && existingEmail.getId() != user.getId()) {
                    errorLabel.setText("This email is already taken.");
                    return;
                }
            }

            user.setUsername(name);
            user.setEmail(email);
            user.setPhone(phone);
            user.setDepartement(departement);
            user.setBirth_date(birthDate);

            if (wantsPasswordChange) {
                if (!PasswordUtil.matches(currentPassword, user.getPassword())) {
                    errorLabel.setText("Current password is incorrect.");
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    errorLabel.setText("New password and confirmation do not match.");
                    return;
                }

                user.setPassword(PasswordUtil.hash(newPassword));
            }

            userDAO.updateUser(user);

            // Update session with new user data
            SessionManager.getInstance().setCurrentUser(user);

            Navigation.navigateToPage("/com/leavemng/views/Profile.fxml", event);
        } catch (SQLException e) {
            errorLabel.setText("Unable to save your profile. Please check your username and email.");
            e.printStackTrace();
        } catch (Exception e) {
            errorLabel.setText("Unable to save your profile. Please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        Navigation.navigateToPage("/com/leavemng/views/Profile.fxml", event);
    }
}
