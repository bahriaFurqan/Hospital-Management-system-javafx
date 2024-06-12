package task.lab_project_oop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin_AddPatientController {

    @FXML
    private Button Add_btn;

    @FXML
    private TableColumn<Patient, String> ContactNo_Column;

    @FXML
    private Button Editbtn;

    @FXML
    private TableColumn<Patient, String> Email_Column;

    @FXML
    private TableColumn<Patient, String> Password_Column;

    @FXML
    private TextField Patient_Contact;

    @FXML
    private TextField Patient_Email;

    @FXML
    private TextField Patient_name;

    @FXML
    private TextField Patient_password;

    @FXML
    private TextField Patient_username;

    @FXML
    private Button Removebtn;

    @FXML
    private TableColumn<Patient, String> Username_Column;

    @FXML
    private Button backbtn;

    @FXML
    private TableColumn<Patient, Integer> id_Column;

    @FXML
    private TableColumn<Patient, String> name_Column;

    @FXML
    private TableView<Patient> tableViewPatients;

    @FXML
    public void initialize() {
        id_Column.setCellValueFactory(new PropertyValueFactory<>("id"));
        Username_Column.setCellValueFactory(new PropertyValueFactory<>("username"));
        Password_Column.setCellValueFactory(new PropertyValueFactory<>("password"));
        name_Column.setCellValueFactory(new PropertyValueFactory<>("name"));
        Email_Column.setCellValueFactory(new PropertyValueFactory<>("email"));
        ContactNo_Column.setCellValueFactory(new PropertyValueFactory<>("contactNo"));

        tableViewPatients.setItems(getAllPatients());
        backbtn.setOnAction(this::back_handler);
        Editbtn.setOnAction(this::Edit_handler);
        Add_btn.setOnAction(this::Add_handler);
        Removebtn.setOnAction(this::Remove_handler);
    }

    @FXML
    void Add_handler(ActionEvent event) {
        String username = Patient_username.getText();
        String password = Patient_password.getText();
        String name = Patient_name.getText();
        String email = Patient_Email.getText();
        String contact = Patient_Contact.getText();
        String user = "Doctor";
        String sql = "INSERT INTO patient (username, password, name, email, ContactNo,user) VALUES (?, ?, ?,?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, name);
            pstmt.setString(4, email);
            pstmt.setString(5, contact);
            pstmt.setString(6, user);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableViewPatients.setItems(getAllPatients()); // Refresh the table
    }

    @FXML
    void Edit_handler(ActionEvent event) {
        Patient selectedPatient = tableViewPatients.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            String username = Patient_username.getText();
            String password = Patient_password.getText();
            String name = Patient_name.getText();
            String email = Patient_Email.getText();
            String contact = Patient_Contact.getText();

            String sql = "UPDATE patient SET username = ?, password = ?, name = ?, email = ?, ContactNo = ? WHERE PatientId = ?";
            try (Connection conn = DatabaseConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.setString(3, name);
                pstmt.setString(4, email);
                pstmt.setString(5, contact);
                pstmt.setInt(6, selectedPatient.getId());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            tableViewPatients.setItems(getAllPatients()); // Refresh the table
        }
    }

    @FXML
    void Remove_handler(ActionEvent event) {
        Patient selectedPatient = tableViewPatients.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            String sql = "DELETE FROM patient WHERE PatientId = ?";
            try (Connection conn = DatabaseConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, selectedPatient.getId());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            tableViewPatients.setItems(getAllPatients()); // Refresh the table
        }
    }

    @FXML
    void back_handler(ActionEvent event) {
        Move_page.navigateToPage(event,"admin_panel.fxml");
    }

    private ObservableList<Patient> getAllPatients() {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        String sql = "SELECT * FROM patient";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Patient patient = new Patient(
                        rs.getInt("PatientId"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("contactNo"));
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }
}