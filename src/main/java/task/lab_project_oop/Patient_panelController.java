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

public class Patient_panelController {

    @FXML
    private Button Appointment_button;



    @FXML
    private Button RequestAppointment_button;



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
    private AnchorPane View_profile_anchor;

    @FXML
    private Label adminid_label;


    @FXML
    private AnchorPane welcome_profile_anchor;

    @FXML
    private Label label_name;

    @FXML
    private Label label_name2;

    @FXML
    private Button logout;


    @FXML
    public void initialize(){
        Appointment_button.setOnAction(this::Appointment_button_handler);
        RequestAppointment_button.setOnAction(this::RequestAppointment_button_handler);
        logout.setOnAction(this::Logouthandler);
        View_profile.setOnAction(this::View_Profile_handler);
        label_name.setText(LoginController.SessionManager.getUsername());
        label_name2.setText(LoginController.SessionManager.getUsername());
        welcome_profile_anchor.setVisible(true);
        View_profile_anchor.setVisible(false);

    }
    @FXML
    void Appointment_button_handler(ActionEvent event) {
        Move_page.navigateToPage(event,"Patient_Appointment.fxml");
    }



    @FXML
    void RequestAppointment_button_handler(ActionEvent event) {
     Move_page.navigateToPage(event,"Request_appointment.fxml");
    }

    @FXML
    void Logouthandler(ActionEvent event) {
        Move_page.navigateToPage(event,"LOGIN_page.fxml");

    }

    @FXML
    void View_Profile_handler(ActionEvent event) {

        welcome_profile_anchor.setVisible(false);
        View_profile_anchor.setVisible(true);
        VIew_profile_username.setText(LoginController.SessionManager.getUsername());
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
                int id = resultSet.getInt("PatientId");
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

}
