package com.leavemng.controllers.admin;

import com.leavemng.dao.LeaveTypeDAO;
import com.leavemng.models.LeaveType;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import com.leavemng.utils.Navigation;
public class ViewLeaveTypesController {

  @FXML
  private TableView<LeaveType> leaveTypesTable;

  @FXML
  private TableColumn<LeaveType, Integer> leaveTypeIdColumn;

  @FXML
  private TableColumn<LeaveType, String> leaveTypeNameColumn;

  @FXML
  private TableColumn<LeaveType, String> leaveTypeDescriptionColumn;

  @FXML
  private TableColumn<LeaveType, Integer> leaveTypeDaysColumn;

  @FXML
  private void handleAddLeaveTypeButtonAction(ActionEvent event) {
    EditLeaveTypeContext.getInstance().setLeaveType(null);
    Navigation.navigateToPage("/com/leavemng/views/admin/EditLeaveType.fxml", event);
  }

  @FXML
  private void handleModifyLeaveTypeButtonAction(ActionEvent event) {
    LeaveType selectedLeaveType = leaveTypesTable.getSelectionModel().getSelectedItem();
    if (selectedLeaveType != null) {
      EditLeaveTypeContext.getInstance().setLeaveType(selectedLeaveType);
      Navigation.navigateToPage("/com/leavemng/views/admin/EditLeaveType.fxml", event);
    }
  }

  @FXML
  private void handleDashboardButtonAction(ActionEvent event) {
    Navigation.navigateToPage("/com/leavemng/views/admin/Dashboard.fxml", event);
  }

  @FXML
  public void handleDeleteLeaveTypeButtonAction(ActionEvent event) {
    // Get the selected LeaveType
    LeaveType selectedLeaveType = leaveTypesTable.getSelectionModel().getSelectedItem();

    if (selectedLeaveType == null) {
      return;
    }

    // Delete the LeaveType
    LeaveTypeDAO leaveTypeDAO = new LeaveTypeDAO();
    leaveTypeDAO.deleteLeaveType(selectedLeaveType);

    // Refresh the TableView
    List<LeaveType> leaveTypes = leaveTypeDAO.getAllLeaveTypes();
    ObservableList<LeaveType> observableList = FXCollections.observableArrayList(
      leaveTypes
    );
    leaveTypesTable.setItems(observableList);
  }

  @FXML
  public void initialize() {
    // Define the cell value factories for the columns
    leaveTypeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    leaveTypeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    leaveTypeDescriptionColumn.setCellValueFactory(
      new PropertyValueFactory<>("description")
    );
    leaveTypeDaysColumn.setCellValueFactory(
      new PropertyValueFactory<>("max_days")
    );

    // Get the data and set it to the TableView
    LeaveTypeDAO leaveTypeDAO = new LeaveTypeDAO();
    List<LeaveType> leaveTypes = leaveTypeDAO.getAllLeaveTypes();

    ObservableList<LeaveType> observableList = FXCollections.observableArrayList(
      leaveTypes
    );
    leaveTypesTable.setItems(observableList);
  }
}
