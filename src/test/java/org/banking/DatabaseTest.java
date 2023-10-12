package org.banking;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class DatabaseTest {
    public static void main(String[] args) throws IOException {

        HashMap<String, String> EnvVariables = EnvParser.parseEnvFile("C:/Users/Vahin/IdeaProjects/BankingProject/.env");

        // Connection parameters
        String url = "jdbc:mysql://" + EnvVariables.get("DATABASE_IP") + ":" + EnvVariables.get("DATABASE_PORT") + "/" + EnvVariables.get("DATABASE_NAME");
        String username = EnvVariables.get("DATABASE_USERNAME");
        String password = EnvVariables.get("DATABASE_PASSWORD");

        // Establish a database connection
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database!");

            // Your database operations go here

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Connection failed. Check the connection parameters.");
        } finally {
            // Close the connection when you're done
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Connection closed.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

