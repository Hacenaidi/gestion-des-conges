module com.leavemng {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base; // Add this line
    requires jakarta.mail;
    requires jakarta.activation;

    opens com.leavemng.controllers to javafx.fxml;
    opens com.leavemng.models to javafx.base;
    exports com.leavemng.controllers.admin to javafx.fxml;

    opens com.leavemng to javafx.fxml;
    opens com.leavemng.controllers.admin to javafx.fxml;

    
    exports com.leavemng;
}