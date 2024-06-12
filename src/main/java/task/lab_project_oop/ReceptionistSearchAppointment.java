package task.lab_project_oop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReceptionistSearchAppointment {

    @FXML
    private TextField ID_textfield;

    @FXML
    private ComboBox<String> Patient_Combobox;

    @FXML
    private Button Searchbutton;

    @FXML
    private TableColumn<Appointment, String> appointmentNoColumn;

    @FXML
    private Button backbtn;

    @FXML
    private TableColumn<Appointment, String> dayColumn;

    @FXML
    private TableColumn<Appointment, String> doctorColumn;

    @FXML
    private TableColumn<Appointment, String> patientColumn;

    @FXML
    private TableView<Appointment> tableViewAppointments;

    @FXML
    private TableColumn<Appointment, String> timeColumn;

    private ObservableList<String> patients = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private Connection connection;

    public ReceptionistSearchAppointment() {
        // Initialize database connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospitalmanagementsystem_", "root", "swabiyousafzai@furqan");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Set action for back button
        backbtn.setOnAction(this::back_handler);

        // Set action for search button
        Searchbutton.setOnAction(this::searchButtonHandler);

        // Initialize TableView columns
        appointmentNoColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentNo"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Populate ComboBox from database
        populateComboBox();

        // Initialize TableView with appointments from database
        tableViewAppointments.setItems(appointments);
        loadAllAppointments();
    }

    private void populateComboBox() {
        try {
            String query = "SELECT Name FROM patient";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                patients.add(resultSet.getString("name"));
            }
            Patient_Combobox.setItems(patients);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAllAppointments() {
        appointments.clear();
        try {
            String query = "SELECT * FROM appointment";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String appointmentId = resultSet.getString("appointment_id");
                String date = resultSet.getString("date");
                String doctor = resultSet.getString("doctor");
                String patient = resultSet.getString("patient");
                String time = resultSet.getString("time");

                // Debug prints to check the data
                System.out.println("Appointment ID: " + appointmentId);
                System.out.println("Date: " + date);
                System.out.println("Doctor: " + doctor);
                System.out.println("Patient: " + patient);
                System.out.println("Time: " + time);

                Appointment appointment = new Appointment(appointmentId, date, doctor, patient, time);
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void searchButtonHandler(ActionEvent event) {
        String searchText = ID_textfield.getText().trim();
        String selectedPatient = Patient_Combobox.getValue();
        searchAppointments(searchText, selectedPatient);
    }

    private void searchAppointments(String searchText, String selectedPatient) {
        appointments.clear();
        try {
            String query = "SELECT * FROM appointment WHERE 1=1";
            if (!searchText.isEmpty()) {
                query += " AND appointment_id LIKE ?";
            }
            if (selectedPatient != null && !selectedPatient.isEmpty()) {
                query += " AND patient = ?";
            }

            PreparedStatement statement = connection.prepareStatement(query);
            int parameterIndex = 1;
            if (!searchText.isEmpty()) {
                statement.setString(parameterIndex++, "%" + searchText + "%");
            }
            if (selectedPatient != null && !selectedPatient.isEmpty()) {
                statement.setString(parameterIndex, selectedPatient);
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String appointmentId = resultSet.getString("appointment_id");
                String date = resultSet.getString("date");
                String doctor = resultSet.getString("doctor");
                String patient = resultSet.getString("patient");
                String time = resultSet.getString("time");

                // Debug prints to check the data
                System.out.println("Search Appointment ID: " + appointmentId);
                System.out.println("Search Date: " + date);
                System.out.println("Search Doctor: " + doctor);
                System.out.println("Search Patient: " + patient);
                System.out.println("Search Time: " + time);

                Appointment appointment = new Appointment(appointmentId, date, doctor, patient, time);
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void back_handler(ActionEvent event) {
        Move_page.navigateToPage(event, "Receptionist_panel.fxml");
    }

    @FXML
    void Patient_Comboboxhandler(ActionEvent event) {
        // Handle ComboBox selection event if needed
    }
}
