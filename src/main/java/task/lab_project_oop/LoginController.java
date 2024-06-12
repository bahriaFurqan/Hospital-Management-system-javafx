package task.lab_project_oop;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class LoginController {
    @FXML
    private Label label;
    @FXML
    private Button login_button;

    @FXML
    private PasswordField login_password;
    @FXML
    private Hyperlink login_registerhere_link;
    @FXML
    private ComboBox<String> login_user;
    @FXML
    private TextField login_username;


    @FXML
    public void initialize() {
        login_user.getItems().addAll("Admin", "Receptionist", "Doctor", "Patient");

        login_button.setOnAction(this::login_handler);
        login_registerhere_link.setOnAction(this::register_page_handler);
        login_user.setOnAction(this::Combobox_handler);
    }

    @FXML
    private void login_handler(ActionEvent actionEvent) {
        String username = login_username.getText();
        String password = login_password.getText();

        String user = login_user.getValue();
        SessionManager.setUser(username, password,user);
        if (validateLogin(username, password, user)) {

            Allertmessage.show("Login Successful");
            if (user.equals("Admin"))
                Move_page.navigateToPage(actionEvent, "Admin_panel.fxml");
            else if (user.equals("Receptionist"))
                Move_page.navigateToPage(actionEvent, "Receptionist_panel.fxml");
            else if (user.equals("Doctor"))
                Move_page.navigateToPage(actionEvent, "Doctor_panel.fxml");
            else if (user.equals("Patient"))
                Move_page.navigateToPage(actionEvent, "Patient_Panel.fxml");
        } else {
            Allertmessage.show("Invalid username, password, or user type.");
        }
    }

    @FXML
    private void register_page_handler(ActionEvent actionEvent) {
        Move_page.navigateToPage(actionEvent, "Register_page.fxml");
    }

    @FXML
    private void Combobox_handler(ActionEvent actionEvent) {
        if (login_user.getValue().equals("Admin")) {
            label.setText("Admin");
        } else if (login_user.getValue().equals("Receptionist")) {
            label.setText("Receptionist");
        } else if (login_user.getValue().equals("Doctor")) {
            label.setText("Doctor");
        } else if (login_user.getValue().equals("Patient")) {
            label.setText("Patient");
        }
    }
    public class SessionManager {
        private static String username;
        private static String password;
        private static String user;


        public static void setUser(String username, String password, String user){
            SessionManager.username = username;
              SessionManager.user = user;
            SessionManager.password = password;
        }

        public static String getUsername() {
            return username;
        }
        public static String getUser() {
            return user;
        }
        public static String getPassword() {
            return password;
        }
    }
    private boolean validateLogin(String username, String password, String user) {
        String query = null;
        if(Objects.equals(user, "Admin"))
        {
            query = "SELECT * FROM admin WHERE username = ? AND password = ? AND user = ?";
        }
        else if(Objects.equals(user, "Receptionist"))
        {
            query = "SELECT * FROM receptionist WHERE username = ? AND password = ? AND user = ?";
        }
        else if(Objects.equals(user, "Doctor"))
        {
            query = "SELECT * FROM doctor WHERE username = ? AND password = ? AND user = ?";
        }
        else if(Objects.equals(user, "Patient"))
        {

            query = "SELECT * FROM patient WHERE username = ? AND password = ? AND user = ?";
        }

        try (Connection connection = DatabaseConnector.getConnection(); // Assuming DatabaseConnector is your class
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, user);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
