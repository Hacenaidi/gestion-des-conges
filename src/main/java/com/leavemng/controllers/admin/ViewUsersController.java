package com.leavemng.controllers.admin;

import com.leavemng.dao.UserDAO;
import com.leavemng.models.User;
import com.leavemng.utils.Navigation;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

public class ViewUsersController {

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> phoneColumn;

    @FXML
    private TableColumn<User, String> departementColumn;

    @FXML
    private TableColumn<User, Integer> annualBalanceColumn;

    @FXML
    private TableColumn<User, Boolean> adminColumn;

        @FXML
        private TableColumn<User, Void> deleteColumn;

    @FXML
    private TextField balanceField;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleDashboardButtonAction(ActionEvent event) {
        Navigation.navigateToPage("/com/leavemng/views/admin/Dashboard.fxml", event);
    }

    @FXML
    private void handleUpdateBalanceButtonAction() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            messageLabel.setText("Please select a user first.");
            return;
        }

        try {
            int newBalance = Integer.parseInt(balanceField.getText());
            UserDAO userDAO = new UserDAO();
            userDAO.updateAnnualBalance(selectedUser.getId(), newBalance);
            selectedUser.setAnnual_balance(newBalance);
            usersTable.refresh();
            messageLabel.setText("Balance updated successfully.");
        } catch (NumberFormatException e) {
            messageLabel.setText("Please enter a valid number.");
        } catch (SQLException e) {
            messageLabel.setText("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void handleLoadSelectedBalanceButtonAction() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            messageLabel.setText("Please select a user first.");
            return;
        }

        balanceField.setText(String.valueOf(selectedUser.getAnnual_balance()));
        messageLabel.setText("Loaded balance for " + selectedUser.getUsername() + ".");
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        departementColumn.setCellValueFactory(new PropertyValueFactory<>("departement"));
        annualBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("annual_balance"));
        adminColumn.setCellValueFactory(new PropertyValueFactory<>("is_admin"));
        usersTable.setPlaceholder(new Label("No users found."));

            deleteColumn.setCellFactory(new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
                @Override
                public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                    final TableCell<User, Void> cell = new TableCell<User, Void>() {
                        private final Button btn = new Button("Delete");
                        {
                            btn.setOnAction(event -> {
                                User user = getTableView().getItems().get(getIndex());
                                handleDeleteUser(user);
                            });
                        }
                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(btn);
                            }
                        }
                    };
                    return cell;
                }
            });

        try {
            UserDAO userDAO = new UserDAO();
            List<User> users = userDAO.getAllNonAdminUsers();
            ObservableList<User> observableUsers = FXCollections.observableArrayList(users);
            usersTable.setItems(observableUsers);
        } catch (SQLException e) {
            messageLabel.setText("Failed to load users.");
            e.printStackTrace();
        }
    }

        private void handleDeleteUser(User user) {
            try {
                UserDAO userDAO = new UserDAO();
                userDAO.deleteUser(user.getId());
                usersTable.getItems().remove(user);
                messageLabel.setText("User " + user.getUsername() + " deleted successfully.");
            } catch (SQLException e) {
                messageLabel.setText("Unable to delete user: " + e.getMessage());
                e.printStackTrace();
            }
        }
}