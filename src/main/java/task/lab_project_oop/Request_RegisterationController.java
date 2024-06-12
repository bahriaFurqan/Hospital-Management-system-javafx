package task.lab_project_oop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Request_RegisterationController {

    @FXML
    private Button Add_btn;

    @FXML
    private TableColumn<Request_register, String> ContactNo_Column;

    @FXML
    private TableColumn<Request_register, String> Email_Column;

    @FXML
    private TableColumn<Request_register, String> Password_Column;

    @FXML
    private Button Removebtn;

    @FXML
    private TableColumn<Request_register, String> Username_Column;

    @FXML
    private Button backbtn;

    @FXML
    private TableColumn<Request_register, String> name_Column;

    @FXML
    private TableView<Request_register> tableViewrequest;

    @FXML
    private TableColumn<Request_register, String> user_Column;

    private ObservableList<Request_register> requestList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        Username_Column.setCellValueFactory(new PropertyValueFactory<>("username"));
        Password_Column.setCellValueFactory(new PropertyValueFactory<>("password"));
        name_Column.setCellValueFactory(new PropertyValueFactory<>("name"));
        Email_Column.setCellValueFactory(new PropertyValueFactory<>("email"));
        ContactNo_Column.setCellValueFactory(new PropertyValueFactory<>("contact"));
        user_Column.setCellValueFactory(new PropertyValueFactory<>("userType"));

        Removebtn.setOnAction(this::Remove_handler);
        Add_btn.setOnAction(this::Add_handler);
        backbtn.setOnAction(this::back_handler);

        // Load initial data from the database
        tableViewrequest.setItems(getAllUsers());
    }

    @FXML
    void Add_handler(ActionEvent event) {
        Request_register request_register = tableViewrequest.getSelectionModel().getSelectedItem();
        if (request_register != null) {
            String username = request_register.getUsername();
            String password = request_register.getPassword();
            String name = request_register.getName();
            String email = request_register.getEmail();
            String contact = request_register.getContact();
            String user = request_register.getUserType();

            // Debugging output to check the user type and values
            System.out.println("Selected User: " + username + ", User Type: " + user);

            String query;
            if ("Admin".equals(user)) {
                query = "INSERT INTO admin (Username, Password, Name, Email, ContactNo, user) VALUES (?, ?, ?, ?, ?, ?)";
            } else if ("Receptionist".equals(user)) {
                query = "INSERT INTO receptionist (Username, Password, Name, Email, ContactNo, user) VALUES (?, ?, ?, ?, ?, ?)";
            } else if ("Doctor".equals(user)) {
                query = "INSERT INTO doctor (Username, Password, Name, Email, ContactNo, user) VALUES (?, ?, ?, ?, ?, ?)";
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid User Type", "The user type is not recognized.");
                return;
            }

            try (Connection connection = DatabaseConnector.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                // Set parameters for the prepared statement
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, name);
                statement.setString(4, email);
                statement.setString(5, contact);
                statement.setString(6, user);

                // Execute the update
                statement.executeUpdate();

                // Remove the selected user from the original table
                removeFromUsersTable(request_register);

                // Refresh the table view
                tableViewrequest.setItems(getAllUsers());

                showAlert(Alert.AlertType.INFORMATION, "Success", "User added successfully.");
            } catch (SQLException throwables) {
                // Print detailed error information
                throwables.printStackTrace();
                String errorMessage = throwables.getMessage();
                showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while adding the user. Details: " + errorMessage);
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to add.");
        }
    }



    @FXML
    void Remove_handler(ActionEvent event) {
        Request_register request_register = tableViewrequest.getSelectionModel().getSelectedItem();
        if (request_register != null) {
            // Remove the selected user from the original table
            removeFromUsersTable(request_register);

            // Refresh the table view
            tableViewrequest.setItems(getAllUsers());

            showAlert(Alert.AlertType.INFORMATION, "Success", "User removed successfully.");
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to remove.");
        }
    }

    @FXML
    void back_handler(ActionEvent event) {
        Move_page.navigateToPage(event, "admin_panel.fxml");
    }

    private void removeFromUsersTable(Request_register request_register) {
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM request_for_approval_login WHERE Username = ?")) {

            statement.setString(1, request_register.getUsername());
            statement.executeUpdate();

            requestList.remove(request_register);
        } catch (SQLException throwables) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while removing the user.");
            throwables.printStackTrace();
        }
    }

    private ObservableList<Request_register> getAllUsers() {
        requestList.clear();

        // Attempt to connect to the database and retrieve the data
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM request_for_approval_login");
             ResultSet resultSet = statement.executeQuery()) {

            // Iterate through the result set and populate the requestList
            while (resultSet.next()) {
                String username = resultSet.getString("Username");
                String password = resultSet.getString("Password");
                String name = resultSet.getString("Name");
                String email = resultSet.getString("Email");
                String contact = resultSet.getString("ContactNo");
                String userType = resultSet.getString("User_Type");

                // Add the new Request_register object to the requestList
                Request_register user = new Request_register(username, password, name, email, contact, userType);
                requestList.add(user);
            }
        } catch (SQLException throwables) {
            // Print stack trace to console for debugging
            throwables.printStackTrace();

            // Show a detailed error alert to the user
            String errorMessage = throwables.getMessage();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while fetching users. Details: " + errorMessage);
        }

        // Return the populated list
        return requestList;
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
