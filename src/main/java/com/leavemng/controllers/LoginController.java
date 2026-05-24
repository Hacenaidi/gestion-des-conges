package com.leavemng.controllers;

import com.leavemng.dao.UserDAO;
import com.leavemng.models.User;
import com.leavemng.utils.SessionManager;
import com.leavemng.utils.Navigation;
import com.leavemng.utils.PasswordUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.leavemng.utils.UiTheme;

public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        UserDAO userDAO = new UserDAO();
        try {
            User user = userDAO.getUserByEmailIgnoreCase(email);
            if (user != null && PasswordUtil.matches(password, user.getPassword())) {
                if (password.equals(user.getPassword())) {
                    user.setPassword(PasswordUtil.hash(password));
                    userDAO.updateUser(user);
                }

                // Store the logged-in user in the session manager
                SessionManager.getInstance().setCurrentUser(user);

                // Navigate to Dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/leavemng/views/Dashboard.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                UiTheme.apply(scene);
                stage.setScene(scene);
            } else {
                errorLabel.setText("Invalid email or password.");
            }
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
