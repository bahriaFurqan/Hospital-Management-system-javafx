package task.lab_project_oop;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import task.lab_project_oop.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Patient_AppointmentViewController {
    @FXML
    private Button backbtn;


    @FXML
    private TableColumn<Admin_Appointment, Integer> appointmentNoColumn;

    @FXML
    private TableColumn<Admin_Appointment, String> dayColumn;

    @FXML
    private TableColumn<Admin_Appointment, String> doctorColumn;

    @FXML
    private TableColumn<Admin_Appointment, String> patientColumn;

    @FXML
    private TableView<Admin_Appointment> tableViewAppointments;

    @FXML
    private TableColumn<Admin_Appointment, String> timeColumn;

    @FXML
    public void initialize() {
        appointmentNoColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentNo"));
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        String patientName = viewporfile_info(); // Get the patient's name
        tableViewAppointments.setItems(getAllAppointments(patientName));
        backbtn.setOnAction(this::back_handler);
    }
@FXML
void back_handler(ActionEvent event) {
        Move_page.navigateToPage(event,"Patient_Panel.fxml");
}

    private ObservableList<Admin_Appointment> getAllAppointments(String patientName) {
        ObservableList<Admin_Appointment> appointments = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointment WHERE patient = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, patientName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Admin_Appointment appointment = new Admin_Appointment(
                        rs.getInt("appointment_id"),
                        rs.getString("doctor"),
                        rs.getString("patient"),
                        rs.getString("date"),
                        rs.getString("time"));
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
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