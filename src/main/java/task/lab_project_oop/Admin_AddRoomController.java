package task.lab_project_oop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin_AddRoomController {

    @FXML
    private Button Add_btn;

    @FXML
    private Button Editbtn;

    @FXML
    private Button Removebtn;

    @FXML
    private Button backbtn;

    @FXML
    private TableColumn<Room, String> room_column;

    @FXML
    private TableView<Room> tableviewroom;
    @FXML
    private TextField Room_textfield;
    @FXML
    public void initialize() {
      room_column.setCellValueFactory(new PropertyValueFactory<>("roomColumn"));
        tableviewroom.setItems(getAllRooms());
        Add_btn.setOnAction(this::Add_handler);
        Editbtn.setOnAction(this::Edit_handler);
        Removebtn.setOnAction(this::Remove_handler);
        backbtn.setOnAction(this::back_handler);

    }
    @FXML
    void Add_handler(ActionEvent event) {
        Room room = new Room(); // Create a new room
        room.setRoomColumn(Room_textfield.getText()); // Set the room column from the text field

        String query = "INSERT INTO room (Room) VALUES (?)";
        try {
            Connection connection = DatabaseConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, room.getRoomColumn());
            statement.executeUpdate();
            statement.close();
            connection.close();

            // Clear the text field
            Room_textfield.clear();

            // Refresh the table view
            tableviewroom.setItems(getAllRooms());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    void Edit_handler(ActionEvent event) {
        Room room = tableviewroom.getSelectionModel().getSelectedItem();
        if (room != null) {
            // Edit the room properties here
            // ...

            String query = "UPDATE room SET Room = ? WHERE Room = ?";
            try {
                Connection connection = DatabaseConnector.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, room.getRoomColumn());
                statement.setString(2, room.getRoomColumn());
                statement.executeUpdate();
                statement.close();
                connection.close();

                // Refresh the table view
                tableviewroom.setItems(getAllRooms());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @FXML
    void Remove_handler(ActionEvent event) {
        Room room = tableviewroom.getSelectionModel().getSelectedItem();
        if (room != null) {
            String query = "DELETE FROM room WHERE Room = ?";
            try {
                Connection connection = DatabaseConnector.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, room.getRoomColumn());
                statement.executeUpdate();
                statement.close();
                connection.close();

                // Refresh the table view
                tableviewroom.setItems(getAllRooms());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @FXML
    void back_handler(ActionEvent event) {
        Move_page.navigateToPage(event, "Admin_panel.fxml");
    }

    private ObservableList<Room> getAllRooms() {
        ObservableList<Room> rooms = FXCollections.observableArrayList();
        String query = "SELECT * FROM room";
        try {
            Connection connection = DatabaseConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Room room = new Room();
                room.setRoomColumn(resultSet.getString("Room"));
                rooms.add(room);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rooms;
    }
}