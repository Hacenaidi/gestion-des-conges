package com.leavemng;

import com.leavemng.utils.UiTheme;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/leavemng/views/Home.fxml"));
        Scene scene = new Scene(loader.load());
        UiTheme.apply(scene);
        primaryStage.setTitle("Leave Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
