package com.leavemng.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navigation {
    public static void navigateToPage(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigation.class.getResource(fxmlPath));
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