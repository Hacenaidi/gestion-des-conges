package com.leavemng.controllers.admin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.leavemng.utils.Navigation;
public class DashboardController {

    @FXML
    private void handleViewPendingRequestsButtonAction(ActionEvent event) {
        Navigation.navigateToPage("/com/leavemng/views/admin/PendingRequests.fxml", event);
    }

    @FXML
    private void handleViewUsersButtonAction(ActionEvent event) {
        Navigation.navigateToPage("/com/leavemng/views/admin/ViewUsers.fxml", event);
    }

    @FXML
    private void handleViewLeaveTypesButtonAction(ActionEvent event) {
        Navigation.navigateToPage("/com/leavemng/views/admin/LeaveType.fxml",event);
    }

    @FXML
    private void handleAddTypeEventButtonAction(ActionEvent event) {
        Navigation.navigateToPage("/com/leavemng/views/admin/AddLeaveType.fxml",event);
    }

        @FXML
        private void handleViewProfileButtonAction(ActionEvent event) {
            Navigation.navigateToPage("/com/leavemng/views/admin/AdminProfile.fxml", event);
        }

    @FXML
    private void handleDashboardButtonAction(ActionEvent event) {
      Navigation.navigateToPage("/com/leavemng/views/Dashboard.fxml", event);
    }


    
}
