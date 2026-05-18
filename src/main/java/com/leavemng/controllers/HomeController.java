package com.leavemng.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;
import com.leavemng.utils.UiTheme;



public class HomeController {
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        // Navigate to Login.fxml
        try {
            // Load the Login.fxml file
            Parent loginView = FXMLLoader.load(getClass().getResource("/com/leavemng/views/Login.fxml"));

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene of the stage to the loaded scene
            Scene scene = new Scene(loginView);
            UiTheme.apply(scene);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            }
    }
    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        try {
            // Load the Register.fxml file
            Parent registerView = FXMLLoader.load(getClass().getResource("/com/leavemng/views/Register.fxml"));

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    
            // Set the scene of the stage to the loaded scene
            Scene scene = new Scene(registerView);
            UiTheme.apply(scene);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
