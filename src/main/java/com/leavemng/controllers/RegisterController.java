package com.leavemng.controllers;

import com.leavemng.dao.UserDAO;
import com.leavemng.models.User;
import com.leavemng.utils.PasswordUtil;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import com.leavemng.utils.Navigation;

public class RegisterController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField departementField;
    @FXML
    private TextField birthDateField;
    @FXML
    private Button registerButton;
    @FXML
    private Label errorLabel;

    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        // Handle user registration
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String phone = phoneField.getText().trim();
        String departement = departementField.getText().trim();
        String birthDate = birthDateField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || departement.isEmpty() || birthDate.isEmpty() ) {
            errorLabel.setText("All fields are required.");
            return;
        }

        UserDAO userDAO = new UserDAO();
        try {
            User existingUser = userDAO.getUserByEmailIgnoreCase(email);
            if (existingUser != null) {
                errorLabel.setText("This email is already taken.");
                return;
            }

            User user = new User();
            user.setUsername(name); 
            user.setEmail(email);
            user.setPassword(PasswordUtil.hash(password));
            user.setPhone(phone);
            user.setDepartement(departement);
            user.setBirth_date(birthDate);
            user.setAnnual_balance(30);
            userDAO.saveUser(user);

            errorLabel.setText("Registration completed successfully!");
            // navigate to login.fxml
            Navigation.navigateToPage("/com/leavemng/views/Login.fxml", event);

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Something went wrong. Please try again.");
        }
    }   

    @FXML
    private void handleBackToMainButtonAction(ActionEvent event) {
        Navigation.navigateToPage("/com/leavemng/views/Home.fxml", event);
    }
}