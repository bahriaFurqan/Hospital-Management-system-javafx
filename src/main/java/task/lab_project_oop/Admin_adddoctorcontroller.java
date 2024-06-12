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
import task.lab_project_oop.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin_adddoctorcontroller {

    @FXML
    private Button backbtn;
    @FXML
    private Button Editbtn;
    @FXML
    private Button Add_btn;
    @FXML
    private Button Removebtn;
    @FXML
    private TableColumn<Doctor, String> ContactNo_Column;
    @FXML
    private TableColumn<Doctor, String> Email_Column;
    @FXML
    private TableColumn<Doctor, String> Password_Column;
    @FXML
    private TableColumn<Doctor, String> Username_Column;
    @FXML
    private TableColumn<Doctor, Integer> id_Column;
    @FXML
    private TextField doctor_Contact;
    @FXML
    private TextField doctor_Email;
    @FXML
    private TextField doctor_name;
    @FXML
    private TextField doctor_password;
    @FXML
    private TextField doctor_username;
    @FXML
    private TableColumn<Doctor, String> name_Column;
    @FXML
    private TableView<Doctor> tableViewdoctors;

    @FXML
    public void initialize() {
        id_Column.setCellValueFactory(new PropertyValueFactory<>("id"));
        Username_Column.setCellValueFactory(new PropertyValueFactory<>("username"));
        Password_Column.setCellValueFactory(new PropertyValueFactory<>("password"));
        name_Column.setCellValueFactory(new PropertyValueFactory<>("name"));
        Email_Column.setCellValueFactory(new PropertyValueFactory<>("email"));
        ContactNo_Column.setCellValueFactory(new PropertyValueFactory<>("contact"));

        tableViewdoctors.setItems(getAllDoctors());
        backbtn.setOnAction(this::back_handler);
        Editbtn.setOnAction(this::Edit_handler);
        Add_btn.setOnAction(this::Add_handler);
        Removebtn.setOnAction(this::Remove_handler);
    }

    @FXML
    void back_handler(ActionEvent event) {
        Move_page.navigateToPage(event,"admin_panel.fxml");
    }

    @FXML
    void Edit_handler(ActionEvent event) {
        Doctor selectedDoctor = tableViewdoctors.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null) {
            String username = doctor_username.getText();
            String password = doctor_password.getText();
            String name = doctor_name.getText();
            String email = doctor_Email.getText();
            String contact = doctor_Contact.getText();

            String sql = "UPDATE doctor SET Password = ?, Name = ?, Email = ?, ContactNo = ? WHERE id = ?";
            try (Connection conn = DatabaseConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, password);
                pstmt.setString(2, name);
                pstmt.setString(3, email);
                pstmt.setString(4, contact);
                pstmt.setInt(5, selectedDoctor.getId());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            tableViewdoctors.setItems(getAllDoctors()); // Refresh the table
        }
    }

    @FXML
    void Remove_handler(ActionEvent event) {
        Doctor selectedDoctor = tableViewdoctors.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null) {
            String sql = "DELETE FROM doctor WHERE id = ?";
            try (Connection conn = DatabaseConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, selectedDoctor.getId());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            tableViewdoctors.setItems(getAllDoctors()); // Refresh the table
        }
    }

    @FXML
    void Add_handler(ActionEvent event) {
        String username = doctor_username.getText();
        String password = doctor_password.getText();
        String name = doctor_name.getText();
        String email = doctor_Email.getText();
        String contact = doctor_Contact.getText();
        String user = "Doctor";

        String sql = "INSERT INTO doctor (username, password, name, email, ContactNo,user) VALUES (?, ?, ?,?, ?, ?)";
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

        tableViewdoctors.setItems(getAllDoctors()); // Refresh the table
    }

    private ObservableList<Doctor> getAllDoctors() {
        ObservableList<Doctor> doctors = FXCollections.observableArrayList();
        String sql = "SELECT * FROM doctor";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Doctor doctor = new Doctor(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("contactNo"));
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }
}