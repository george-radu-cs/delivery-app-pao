package main.java.com.griosoft.delivery.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection = null;

    private DatabaseConnection() {
    }

    public static Connection getInstance() throws SQLException, ClassNotFoundException {
        if (connection == null) {
            String url = "jdbc:mysql://localhost:3306/delivery_pao?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
            String user = "delivery";
            String password = "Sup3rSecret21!";
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }
}
