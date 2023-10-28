package org.database;
import org.banking.EnvParser;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.ArrayList;

public class Database_Functions {
    public static HashMap<Integer, HashMap<String, String>> retrieveData(String sql) throws SQLException {
        HashMap<Integer, HashMap<String, String>> result = new HashMap<>();
        Connection connection = null;
        try {
            // import details from .env file
            HashMap<String, String> connectionDetails = EnvParser.parseEnvFile("C:/Users/Vahin/IdeaProjects/BankingProject/.env");

            // Set connection parameters
            String url = "jdbc:mysql://"
                    + connectionDetails.get("DATABASE_IP")
                    + ":"
                    + connectionDetails.get("DATABASE_PORT")
                    + "/" + connectionDetails.get("DATABASE_NAME");

            String username = connectionDetails.get("DATABASE_USERNAME");
            String password = connectionDetails.get("DATABASE_PASSWORD");

            // Establish a database connection
            connection = DriverManager.getConnection(url, username, password);
            // Create a SQL statement
            PreparedStatement statement = connection.prepareStatement(sql);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();
            int rowNumber = 0;

            while (resultSet.next()) {
                rowNumber++;
                HashMap<String, String> row = new HashMap<>();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    String columnValue = resultSet.getString(i);
                    row.put(columnName, columnValue);
                }
                result.put(rowNumber, row);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving data from the database.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return result;
    }

    public static void insertData(String sql, Object... parameters) throws SQLException {
        Connection connection = null;
        try {
            // import details from .env file
            HashMap<String, String> connectionDetails = EnvParser.parseEnvFile("C:/Users/Vahin/IdeaProjects/BankingProject/.env");

            // Set connection parameters
            String url = "jdbc:mysql://"
                    + connectionDetails.get("DATABASE_IP")
                    + ":"
                    + connectionDetails.get("DATABASE_PORT")
                    + "/" + connectionDetails.get("DATABASE_NAME");

            String username = connectionDetails.get("DATABASE_USERNAME");
            String password = connectionDetails.get("DATABASE_PASSWORD");

            // Establish a database connection
            connection = DriverManager.getConnection(url, username, password);
            // Create a SQL statement with parameter placeholders
            PreparedStatement statement = connection.prepareStatement(sql);
            // Set parameters (new values)
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }

            // Execute the insert
            int rowsAffected = statement.executeUpdate();
            System.out.println("Inserted " + rowsAffected + " row(s).");
        } catch (SQLException e) {
            System.err.println("Error inserting data into the database.");
            throw new SQLIntegrityConstraintViolationException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void updateData(String sql, Object... parameters) throws SQLException {
        Connection connection = null;
        try {
            // import details from .env file
            HashMap<String, String> connectionDetails = EnvParser.parseEnvFile("C:/Users/Vahin/IdeaProjects/BankingProject/.env");

            // Set connection parameters
            String url = "jdbc:mysql://"
                    + connectionDetails.get("DATABASE_IP")
                    + ":"
                    + connectionDetails.get("DATABASE_PORT")
                    + "/" + connectionDetails.get("DATABASE_NAME");

            String username = connectionDetails.get("DATABASE_USERNAME");
            String password = connectionDetails.get("DATABASE_PASSWORD");
            // Establish a database connection
            connection = DriverManager.getConnection(url, username, password);
            // Create a SQL statement with parameter placeholders
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set parameters (new values and the condition)
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }

            // Execute the update
            int rowsAffected = statement.executeUpdate();
            System.out.println("Updated " + rowsAffected + " row(s).");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.err.println("Error updating data in the database.");
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void deleteData(String sql) throws SQLException {
        Connection connection = null;
        try {
            // import details from .env file
            HashMap<String, String> connectionDetails = EnvParser.parseEnvFile("C:/Users/Vahin/IdeaProjects/BankingProject/.env");

            // Set connection parameters
            String url = "jdbc:mysql://"
                    + connectionDetails.get("DATABASE_IP")
                    + ":"
                    + connectionDetails.get("DATABASE_PORT")
                    + "/" + connectionDetails.get("DATABASE_NAME");

            String username = connectionDetails.get("DATABASE_USERNAME");
            String password = connectionDetails.get("DATABASE_PASSWORD");

            // Establish a database connection
            connection = DriverManager.getConnection(url, username, password);
            // Create a SQL statement with parameter placeholders
            PreparedStatement statement = connection.prepareStatement(sql);

            // Execute the delete
            int rowsAffected = statement.executeUpdate();
            System.out.println("Deleted " + rowsAffected + " row(s).");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

}
