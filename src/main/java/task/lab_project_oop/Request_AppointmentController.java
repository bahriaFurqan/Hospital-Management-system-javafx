package task.lab_project_oop;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import task.lab_project_oop.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Request_AppointmentController{

    @FXML
    private Button Addbtn,  Editbtn, Removebtn, backbtn;

    @FXML
    private ComboBox<String> Doctor_Combobox;

    @FXML
    private TextField Time_textfield;

    @FXML
    private DatePicker date_picker;

    @FXML
    private TableView<Admin_Appointment> tableViewAppointments;

    @FXML
    private TableColumn<Admin_Appointment,String> appointmentNoColumn, dayColumn, doctorColumn, patientColumn, timeColumn;

    private ObservableList<Admin_Appointment> appointmentList = FXCollections.observableArrayList();

    public String convertTimeFormat(String oldFormatTime) {
        try {
            DateTimeFormatter oldFormat;
            if (oldFormatTime.contains(":")) {
                oldFormat = DateTimeFormatter.ofPattern("h:mma");
            } else {
                oldFormat = DateTimeFormatter.ofPattern("ha");
            }
            DateTimeFormatter newFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime time = LocalTime.parse(oldFormatTime, oldFormat);
            return time.format(newFormat);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format");
            return null;
        }
    }

    @FXML
    public void initialize() {

        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        Addbtn.setOnAction(this::Add_handler);
        Editbtn.setOnAction(this::Edit_handler);
        Removebtn.setOnAction(this::Remove_handler);
        backbtn.setOnAction(this::back_handler);

        Platform.runLater(() -> {
            ObservableList<String> doctornames = getAlldoctornames();
            Doctor_Combobox.setItems(doctornames);
        });

        String patient = viewporfile_info();
        loadAppointments(patient);
    }

    @FXML

    void Add_handler(ActionEvent event) {
        String patient = viewporfile_info();// The patient's name
        String doctor = Doctor_Combobox.getSelectionModel().getSelectedItem();
        String date = date_picker.getValue().toString();
        String time = Time_textfield.getText();
        String newFormatTime = convertTimeFormat(time);
        if (newFormatTime != null) {
            time = newFormatTime;
        }

        String sql = "INSERT INTO appointment_request(doctor, patient, date, time) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, doctor);
            pstmt.setString(2, patient);
            pstmt.setString(3, date);
            pstmt.setString(4, time);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadAppointments(patient);
    }

    @FXML
    void Edit_handler(ActionEvent event) {
        String patient = viewporfile_info(); // The patient's name
        String doctor = Doctor_Combobox.getSelectionModel().getSelectedItem();
        String date = date_picker.getValue().toString();
        String time = Time_textfield.getText();
        String newFormatTime = convertTimeFormat(time);
        if (newFormatTime != null) {
            time = newFormatTime;
        }

        String sql = "UPDATE appointment_request SET doctor = ?, date = ?, time = ? WHERE patient = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, doctor);
            pstmt.setString(2, date);
            pstmt.setString(3, time);
            pstmt.setString(4, patient);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadAppointments(patient);
    }

    @FXML
    void Remove_handler(ActionEvent event) {
        String patient = viewporfile_info();// The patient's name
        String sql = "DELETE FROM appointment_request WHERE patient = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, patient);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadAppointments(patient);
    }

    @FXML
    void back_handler(ActionEvent event) {
        Move_page.navigateToPage(event,"Patient_Panel.fxml");
    }

    private ObservableList<String> getAlldoctornames() {
        ObservableList<String> doctornames = FXCollections.observableArrayList();
        String sql = "SELECT Name FROM doctor";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("Name");
                doctornames.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctornames;
    }



    private void loadAppointments(String patient) {
        tableViewAppointments.getItems().clear();


        String sql = "SELECT * FROM appointment_request WHERE patient = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, patient);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String time = rs.getString("time");
                DateTimeFormatter oldFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
                DateTimeFormatter newFormat = DateTimeFormatter.ofPattern("h:mma");
                LocalTime localTime = LocalTime.parse(time, oldFormat);
                time = localTime.format(newFormat);

                Admin_Appointment appointment = new Admin_Appointment(
                        rs.getString("doctor"),
                        rs.getString("patient"),
                        rs.getString("date"),
                        time);
                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableViewAppointments.setItems(appointmentList); // Set the items of the TableView
    }

    private String viewporfile_info() {
        String patientName = null;
        try {
            String username = LoginController.SessionManager.getUsername();
            String password = LoginController.SessionManager.getPassword();
            String user = "Patient";
            String query = "SELECT * FROM patient WHERE Username = ? AND Password = ? AND user = ?";
            Connection connection = DatabaseConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, user);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                patientName = resultSet.getString("Name");

            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientName;
    }
}