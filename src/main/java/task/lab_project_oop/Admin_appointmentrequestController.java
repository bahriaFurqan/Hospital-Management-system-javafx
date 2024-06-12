package task.lab_project_oop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin_appointmentrequestController {

    @FXML
    private Button Add_btn;

    @FXML
    private TableColumn<Admin_Requestappoinment, String> Date_Column;

    @FXML
    private TableColumn<Admin_Requestappoinment, String> Doctor_Column;

    @FXML
    private TableColumn<Admin_Requestappoinment, String> Patient_Column;

    @FXML
    private Button Removebtn;

    @FXML
    private TableColumn<Admin_Requestappoinment, String> Time_Column;

    @FXML
    private Button backbtn;

    @FXML
    private TableView<Admin_Requestappoinment> tableViewrequestappointment;

    // Initialize the controller after the FXML file has been loaded
    @FXML
    public void initialize() {
        // Initialize column bindings with the correct property names
        Doctor_Column.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        Patient_Column.setCellValueFactory(new PropertyValueFactory<>("patient"));
        Date_Column.setCellValueFactory(new PropertyValueFactory<>("date"));
        Time_Column.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Set up the back button handler
        backbtn.setOnAction(this::back_handler);

        // Load appointment requests into the table
        loadAppointmentRequest();
    }

    // Load appointment requests from the database into the TableView
    private void loadAppointmentRequest() {
        tableViewrequestappointment.getItems().clear();
        String query = "SELECT doctor, patient, date, time FROM appointment_request";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String doctor = resultSet.getString("doctor");
                String patient = resultSet.getString("patient");
                String date = resultSet.getString("date");
                String time = resultSet.getString("time");

                // Add data to the TableView
                tableViewrequestappointment.getItems().add(new Admin_Requestappoinment(doctor, patient, date, time));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Handle the Add button click event to move the selected appointment to another table
    @FXML
    void Add_handler(ActionEvent event) {
        Admin_Requestappoinment selectedAppointment = tableViewrequestappointment.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            String insertQuery = "INSERT INTO appointment (doctor, patient, date, time) VALUES (?, ?, ?, ?)";
            try (Connection connection = DatabaseConnector.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

                preparedStatement.setString(1, selectedAppointment.getDoctor());
                preparedStatement.setString(2, selectedAppointment.getPatient());
                preparedStatement.setString(3, selectedAppointment.getDate());
                preparedStatement.setString(4, selectedAppointment.getTime());

                preparedStatement.executeUpdate();
                tableViewrequestappointment.getItems().remove(selectedAppointment); // Optionally remove from table

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No appointment selected.");
        }
    }

    // Handle the Remove button click event
    @FXML
    void Remove_handler(ActionEvent event) {
        Admin_Requestappoinment selectedAppointment = tableViewrequestappointment.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            String deleteQuery = "DELETE FROM appointment_request WHERE patient = ?";
            try (Connection connection = DatabaseConnector.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

                preparedStatement.setString(1, selectedAppointment.getPatient());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    tableViewrequestappointment.getItems().remove(selectedAppointment);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No appointment selected.");
        }
    }

    // Handle the back button click event to navigate to another page
    @FXML
    void back_handler(ActionEvent event) {
        Move_page.navigateToPage(event, "admin_Appointment.fxml");
    }
}
