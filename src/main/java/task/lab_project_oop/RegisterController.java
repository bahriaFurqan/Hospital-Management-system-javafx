package task.lab_project_oop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private TextField Contact_textfield;

    @FXML
    private TextField Full_name_textfield;

    @FXML
    private ComboBox<String> Select_user;

    @FXML
    private Label label_fill;

    @FXML
    private Button register_button;

    @FXML
    private TextField register_email;

    @FXML
    private Hyperlink register_loginhere_link;

    @FXML
    private PasswordField register_password;

    @FXML
    private TextField register_username;

    @FXML
    public void initialize() {
        Select_user.getItems().addAll("Admin", "Receptionist", "Doctor", "Patient");
        Select_user.setOnAction(this::Combobox_handler);
        register_button.setOnAction(this::Register_userhandler);
        register_loginhere_link.setOnAction(this::Loginpage_handler);

    }

    @FXML
    void Combobox_handler(ActionEvent event) {

        if (Select_user.getValue().equals("Admin")) {
            label_fill.setText("Admin");
        }
        else if (Select_user.getValue().equals("Receptionist")) {
            label_fill.setText("Receptionist");
        }
        else if (Select_user.getValue().equals("Doctor")) {
            label_fill.setText("Doctor");
        }
        else if (Select_user.getValue().equals("Patient")) {
            label_fill.setText("Patient");
        }
    }

    @FXML
    void Loginpage_handler(ActionEvent event) {
        Move_page.navigateToPage(event, "LOGIN_page.fxml");
    }

    @FXML
    void Register_userhandler(ActionEvent event) {
        String username = register_username.getText();
        String email = register_email.getText();
        String password = register_password.getText();
        String user = Select_user.getValue();
        String contact = Contact_textfield.getText();
        String name = Full_name_textfield.getText();



        if (register_username.getText().isEmpty() || register_password.getText().isEmpty() || register_email.getText().isEmpty() || Contact_textfield.getText().isEmpty() || Full_name_textfield.getText().isEmpty()) {
            label_fill.setText("Please fill all the fields");

        }
        else {
            Register register = new Register(username, email, password, user, contact, name);
            if (registerUser(register)) {
                Allertmessage.show("Registration Successful");
                Move_page.navigateToPage(event, "Login.fxml");
            } else {
                Allertmessage.show("Registration Failed");
            }
        }

    }

    private boolean registerUser(Register register) {
        if (register.getUser() == "Admin" || register.getUser() == "Receptionist" || register.getUser() == "Doctor") {
            String query = "INSERT INTO request_for_approval_login (Username, Email, Password, user_type,Name,ContactNo) VALUES ( ?, ?, ?,?,?,?)";

            try (Connection connection = DatabaseConnector.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, register.getUsername());
                preparedStatement.setString(2, register.getEmail());
                preparedStatement.setString(3, register.getPassword());
                preparedStatement.setString(4, register.getUser());
                preparedStatement.setString(5, register.getFullname());
                preparedStatement.setString(6, register.getContact());


                int result = preparedStatement.executeUpdate();
                return result > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else if (register.getUser() == "Patient") {
            String query = "INSERT INTO patient (Username, Email, Password, user_type,Name,ContactNo) VALUES ( ?, ?, ?,?,?,?)";

            try (Connection connection = DatabaseConnector.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, register.getUsername());
                preparedStatement.setString(2, register.getEmail());
                preparedStatement.setString(3, register.getPassword());
                preparedStatement.setString(4, register.getUser());
                preparedStatement.setString(5, register.getFullname());
                preparedStatement.setString(6, register.getContact());

                int result = preparedStatement.executeUpdate();
                return result > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}

