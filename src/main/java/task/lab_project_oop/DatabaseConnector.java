package task.lab_project_oop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/hospitalmanagementsystem_";
    private static final String USER = "root";
    private static final String PASSWORD = "swabiyousafzai@furqan";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}