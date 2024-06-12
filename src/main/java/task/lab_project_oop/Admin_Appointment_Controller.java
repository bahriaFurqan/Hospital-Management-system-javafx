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

public class Admin_Appointment_Controller {

    @FXML
    private Button Add_btn, Appointmentbtn, Editbtn, Removebtn, backbtn,Appointment_button;

    @FXML
    private ComboBox<String> Doctor_Combobox, Patient_Combobox;

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
        appointmentNoColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentNo"));
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        Add_btn.setOnAction(this::Add_handler);
        Editbtn.setOnAction(this::Edit_handler);
        Removebtn.setOnAction(this::Remove_handler);
        backbtn.setOnAction(this::back_handler);
        Appointment_button.setOnAction(this::Appointment_buttonhandler);

        Platform.runLater(() -> {
            ObservableList<String> doctornames = getAlldoctornames();
            ObservableList<String> patientnames = getAllpatientnames();

            Doctor_Combobox.setItems(doctornames);
            Patient_Combobox.setItems(patientnames);
        });

        loadAppointments();
    }

    @FXML
    void Appointment_buttonhandler(ActionEvent event) {
        Move_page.navigateToPage(event,"Admin_appointmentrequest.fxml");
    }
    @FXML
    void Add_handler(ActionEvent event) {
        String doctor = Doctor_Combobox.getSelectionModel().getSelectedItem();
        String patient = Patient_Combobox.getSelectionModel().getSelectedItem();
        String date = date_picker.getValue().toString();
        String time = Time_textfield.getText();
        String newFormatTime = convertTimeFormat(time);
        if (newFormatTime != null) {
            time = newFormatTime;
        }

        String sql = "INSERT INTO appointment(doctor, patient, date, time) VALUES(?,?,?,?)";
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
        loadAppointments();
    }

    @FXML
    void Edit_handler(ActionEvent event) {
        String doctor = Doctor_Combobox.getSelectionModel().getSelectedItem();
        String patient = Patient_Combobox.getSelectionModel().getSelectedItem();
        String date = date_picker.getValue().toString();
        String time = Time_textfield.getText();
        String newFormatTime = convertTimeFormat(time);
        if (newFormatTime != null) {
            time = newFormatTime;
        }

        Admin_Appointment selectedAppointment = tableViewAppointments.getSelectionModel().getSelectedItem();
        int appointmentNo = selectedAppointment.getAppointmentNo();
        String sql = "UPDATE appointment SET doctor = ?, patient = ?, date = ?, time = ? WHERE appointment_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, doctor);
            pstmt.setString(2, patient);
            pstmt.setString(3, date);
            pstmt.setString(4, time);
            pstmt.setInt(5, appointmentNo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadAppointments();
    }

    @FXML
    void Remove_handler(ActionEvent event) {
        Admin_Appointment selectedAppointment = tableViewAppointments.getSelectionModel().getSelectedItem();
        int appointmentNo = selectedAppointment.getAppointmentNo();
        String sql = "DELETE FROM appointment WHERE appointment_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, appointmentNo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadAppointments();
    }

    @FXML
    void back_handler(ActionEvent event) {
        if (LoginController.SessionManager.getUser().equals("Admin")) {
            Move_page.navigateToPage(event,"Admin_panel.fxml");
        } else {
            Move_page.navigateToPage(event,"Receptionist_panel.fxml");
        }

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

    private ObservableList<String> getAllpatientnames() {
        ObservableList<String> patientnames = FXCollections.observableArrayList();
        String sql = "SELECT Name FROM patient";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("Name");
                patientnames.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientnames;
    }

    private void loadAppointments() {
        tableViewAppointments.getItems().clear();
        String sql = "SELECT * FROM appointment";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String time = rs.getString("time");
                DateTimeFormatter oldFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
                DateTimeFormatter newFormat = DateTimeFormatter.ofPattern("h:mma");
                LocalTime localTime = LocalTime.parse(time, oldFormat);
                time = localTime.format(newFormat);

                Admin_Appointment appointment = new Admin_Appointment(rs.getInt("appointment_id"),
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
}