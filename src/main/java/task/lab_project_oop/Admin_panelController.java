package task.lab_project_oop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin_panelController{

    @FXML
    private Button Appointment_button;

    @FXML
    private Button Edit_profile;

    @FXML
    private TextField Email_textfield;

    @FXML
    private TextField Password_textfield;

    @FXML
    private Button Reg_request_button;

    @FXML
    private Button Update_Profile;

    @FXML
    private TextField Username_textfield;

    @FXML
    private Text VIew_profile_Adminid;

    @FXML
    private Text VIew_profile_Name;

    @FXML
    private Text VIew_profile_contact;

    @FXML
    private Text VIew_profile_email;

    @FXML
    private Text VIew_profile_username;

    @FXML
    private Button View_profile;

    @FXML
    private AnchorPane welcome_profile_anchor;
    @FXML
    private AnchorPane View_profile_anchor;

    @FXML
    private Label adminid_label;

    @FXML
    private TextField conatct_textfield1;

@FXML
private Button logout;
    @FXML
    private Button doctor_button;

    @FXML
    private AnchorPane edit_anchor;

    @FXML
    private Label label_name;

    @FXML
    private Label label_name2;

    @FXML
    private TextField name_textfield;

    @FXML
    private Button patient_button;

    @FXML
    private Button Shift_patient;
    @FXML
    private Button Room_button;
    @FXML
    public void initialize(){
        Appointment_button.setOnAction(this::Appointment_handler);
        Reg_request_button.setOnAction(this::reg_request_handler);
        doctor_button.setOnAction(this::doctor_handler);
        patient_button.setOnAction(this::patient_handler);
        logout.setOnAction(this::logouthandler);

        View_profile.setOnAction(this::View_Profile_handler);
        Room_button.setOnAction(this::Room_buttonhandler);
        Shift_patient.setOnAction(this::Shift_patienthandler);
        label_name.setText(LoginController.SessionManager.getUsername());
        label_name2.setText(LoginController.SessionManager.getUsername());
        welcome_profile_anchor.setVisible(true);
        View_profile_anchor.setVisible(false);

    }
    @FXML
    void logouthandler(ActionEvent event) {
        Move_page.navigateToPage(event,"LOGIN_page.fxml");
    }
    void  Appointment_handler(ActionEvent event) {
        Move_page.navigateToPage(event,"admin_Appointment.fxml");

    }

    @FXML
    void View_Profile_handler(ActionEvent event) {
        welcome_profile_anchor.setVisible(false);
        View_profile_anchor.setVisible(true);
        VIew_profile_username.setText(LoginController.SessionManager.getUsername());
        try {
            String username = LoginController.SessionManager.getUsername();
            String password = LoginController.SessionManager.getPassword();
            String user = "Admin";
            String query = "SELECT * FROM admin WHERE Username = ? AND Password = ? AND user = ?";
            Connection connection = DatabaseConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, user);


            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("Name");
                               String email = resultSet.getString("Email");
                String contact = resultSet.getString("ContactNo");

                VIew_profile_Adminid.setText(String.valueOf(id));
                VIew_profile_Name.setText(name);
                VIew_profile_email.setText(email);
                VIew_profile_contact.setText(contact);
                adminid_label.setText(String.valueOf(id));

            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @FXML
    void doctor_handler(ActionEvent event) {
        Move_page.navigateToPage(event,"Admin_Adddoctor.fxml");
    }

    @FXML
    void patient_handler(ActionEvent event) {
        Move_page.navigateToPage(event,"Admin_addPatient.fxml");
    }

    @FXML
    void reg_request_handler(ActionEvent event) {
        Move_page.navigateToPage(event,"Request_Registeration.fxml");
    }
     @FXML
    void Room_buttonhandler(ActionEvent event) {
        Move_page.navigateToPage(event,"Admin_AddRoom.fxml");
    }
    @FXML
    void Shift_patienthandler(ActionEvent event) {
        Move_page.navigateToPage(event,"Shift_pantient_room.fxml");
    }

}
