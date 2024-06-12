package task.lab_project_oop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class Shift_patientroomController implements Initializable {

    @FXML
    private Button Add_btn;

    @FXML
    private ComboBox<String> Doctor_Combobox;

    @FXML
    private Button Editbtn;

    @FXML
    private ComboBox<String> Patient_Combobox;

    @FXML
    private Button Removebtn;

    @FXML
    private ComboBox<String> Room_Combobox;

    @FXML
    private TextField Time_textfield;

    @FXML
    private TableColumn<Shift, Integer> appointmentNoColumn;

    @FXML
    private Button backbtn;

    @FXML
    private DatePicker date_picker;

    @FXML
    private TableColumn<Shift, String> dayColumn;

    @FXML
    private TableColumn<Shift, String> doctorColumn;

    @FXML
    private TableColumn<Shift, String> patientColumn;

    @FXML
    private TableColumn<Shift, String> roomColumn;

    @FXML
    private TableView<Shift> shiftpatienttable;

    @FXML
    private TableColumn<Shift, String> timeColumn;

    private ObservableList<String> doctorsList = FXCollections.observableArrayList();
    private ObservableList<String> patientsList = FXCollections.observableArrayList();
    private ObservableList<String> roomsList = FXCollections.observableArrayList();
    private ObservableList<Shift> shiftList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Removebtn.setOnAction(this::Remove_handler);
        Editbtn.setOnAction(this::Edit_handler);
        Add_btn.setOnAction(this::Add_handler);
        backbtn.setOnAction(this::back_handler);

        populateDoctorComboBox();
        populatePatientComboBox();
        populateRoomComboBox();
        populateTableView();
    }

    private void populateDoctorComboBox() {
        String query = "SELECT Name FROM doctor";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                doctorsList.add(rs.getString("name"));
            }
            Doctor_Combobox.setItems(doctorsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populatePatientComboBox() {
        String query = "SELECT Name FROM patient";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                patientsList.add(rs.getString("name"));
            }
            Patient_Combobox.setItems(patientsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateRoomComboBox() {
        String query = "SELECT Room FROM room";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                roomsList.add(rs.getString("Room"));
            }
            Room_Combobox.setItems(roomsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateTableView() {
        String query = "SELECT * FROM shiftpatient";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            shiftList.clear();
            while (rs.next()) {
                Shift shift = new Shift(
                        rs.getInt("appointmentNo"),
                        rs.getString("Date"),
                        rs.getString("time"),
                        rs.getString("doctor"),
                        rs.getString("patient"),
                        rs.getString("room")
                );
                shiftList.add(shift);
            }
            shiftpatienttable.setItems(shiftList);

            appointmentNoColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentNo"));
            dayColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
            timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
            doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctor"));
            patientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));
            roomColumn.setCellValueFactory(new PropertyValueFactory<>("room"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Add_handler(ActionEvent event) {
        String day = date_picker.getValue().toString();
        String time = Time_textfield.getText();
        String doctor = Doctor_Combobox.getSelectionModel().getSelectedItem();
        String patient = Patient_Combobox.getSelectionModel().getSelectedItem();
        String room = Room_Combobox.getSelectionModel().getSelectedItem();

        if (!isRoomAvailable(room, day, time)) {
            System.out.println("Room is already occupied at this time.");
            return;
        }

        if (isPatientInOtherRoom(patient, day, time)) {
            System.out.println("Patient is already scheduled in another room at this time.");
            return;
        }

        String query = "INSERT INTO shiftpatient (Date, Time, doctor, Patient, Room) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, day);
            stmt.setString(2, time);
            stmt.setString(3, doctor);
            stmt.setString(4, patient);
            stmt.setString(5, room);
            stmt.executeUpdate();
            populateTableView(); // Refresh table view
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Edit_handler(ActionEvent event) {
        Shift selectedShift = shiftpatienttable.getSelectionModel().getSelectedItem();
        if (selectedShift == null) return;

        String day = date_picker.getValue().toString();
        String time = Time_textfield.getText();
        String doctor = Doctor_Combobox.getSelectionModel().getSelectedItem();
        String patient = Patient_Combobox.getSelectionModel().getSelectedItem();
        String room = Room_Combobox.getSelectionModel().getSelectedItem();

        if (!isRoomAvailable(room, day, time, selectedShift.getAppointmentNo())) {
            System.out.println("Room is already occupied at this time.");
            return;
        }

        if (isPatientInOtherRoom(patient, day, time, selectedShift.getAppointmentNo())) {
            System.out.println("Patient is already scheduled in another room at this time.");
            return;
        }

        String query = "UPDATE shiftpatient SET Date = ?, Time = ?, doctor = ?, Patient = ?, Room = ? WHERE appointmentNo = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, day);
            stmt.setString(2, time);
            stmt.setString(3, doctor);
            stmt.setString(4, patient);
            stmt.setString(5, room);
            stmt.setInt(6, selectedShift.getAppointmentNo());
            stmt.executeUpdate();
            populateTableView(); // Refresh table view
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Remove_handler(ActionEvent event) {
        Shift selectedShift = shiftpatienttable.getSelectionModel().getSelectedItem();
        if (selectedShift == null) return;

        String query = "DELETE FROM shiftpatient WHERE appointmentNo = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, selectedShift.getAppointmentNo());
            stmt.executeUpdate();
            populateTableView(); // Refresh table view
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void back_handler(ActionEvent event) {
        if (LoginController.SessionManager.getUser().equals("Admin"))
            Move_page.navigateToPage(event, "admin_panel.fxml");
        else
            Move_page.navigateToPage(event, "Receptionist_panel.fxml");
    }

    private Connection getConnection() throws Exception {
        String url = "jdbc:mysql://127.0.0.1:3306/hospitalmanagementsystem_";
        String user = "root";
        String password = "swabiyousafzai@furqan";
        return DriverManager.getConnection(url, user, password);
    }

    private boolean isRoomAvailable(String room, String day, String time) {
        return isRoomAvailable(room, day, time, -1);
    }

    private boolean isRoomAvailable(String room, String day, String time, int excludeAppointmentNo) {
        String query = "SELECT COUNT(*) FROM shiftpatient WHERE Room = ? AND Date = ? AND Time = ? AND appointmentNo != ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, room);
            stmt.setString(2, day);
            stmt.setString(3, time);
            stmt.setInt(4, excludeAppointmentNo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean isPatientInOtherRoom(String patient, String day, String time) {
        return isPatientInOtherRoom(patient, day, time, -1);
    }

    private boolean isPatientInOtherRoom(String patient, String day, String time, int excludeAppointmentNo) {
        String query = "SELECT COUNT(*) FROM shiftpatient WHERE Patient = ? AND Date = ? AND Time = ? AND appointmentNo != ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, patient);
            stmt.setString(2, day);
            stmt.setString(3, time);
            stmt.setInt(4, excludeAppointmentNo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Define the Shift class for TableView representation
    public static class Shift {
        private int appointmentNo;
        private String day;
        private String time;
        private String doctor;
        private String patient;
        private String room;

        public Shift(int appointmentNo, String day, String time, String doctor, String patient, String room) {
            this.appointmentNo = appointmentNo;
            this.day = day;
            this.time = time;
            this.doctor = doctor;
            this.patient = patient;
            this.room = room;
        }

        public int getAppointmentNo() {
            return appointmentNo;
        }

        public void setAppointmentNo(int appointmentNo) {
            this.appointmentNo = appointmentNo;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDoctor() {
            return doctor;
        }

        public void setDoctor(String doctor) {
            this.doctor = doctor;
        }

        public String getPatient() {
            return patient;
        }

        public void setPatient(String patient) {
            this.patient = patient;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }
    }
}
