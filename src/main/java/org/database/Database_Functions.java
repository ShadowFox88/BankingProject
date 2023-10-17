package org.database;
import java.sql.*;
import java.util.HashMap;
import java.util.ArrayList;

public class Database_Functions {
    public static HashMap<Integer, HashMap<String, String>> retrieveData(ArrayList<String> connection_details, String sql) throws SQLException {
        HashMap<Integer, HashMap<String, String>> result = new HashMap<>();
        Connection connection = null;
        try {
            String url = "jdbc:mysql://" + connection_details.get(0) + ":" + connection_details.get(1) + "/" + connection_details.get(2);
            String username = connection_details.get(3);
            String password = connection_details.get(4);

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
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return result;
    }

    public static void insertData(ArrayList<String> connection_details, String sql, Object... parameters) throws SQLException {
        Connection connection = null;
        try {
            String url = "jdbc:mysql://" + connection_details.get(0) + ":" + connection_details.get(1) + "/" + connection_details.get(2);
            String username = connection_details.get(3);
            String password = connection_details.get(4);

            // Establish a database connection
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database.");
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
            e.printStackTrace();
            System.err.println("Error inserting data into the database.");
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void updateData(ArrayList<String> connection_details, String sql, Object... parameters) throws SQLException {
        Connection connection = null;
        try {
            String url = "jdbc:mysql://" + connection_details.get(0) + ":" + connection_details.get(1) + "/" + connection_details.get(2);
            String username = connection_details.get(3);
            String password = connection_details.get(4);

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
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating data in the database.");
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void deleteData(ArrayList<String> connectionDetails, String sql) throws SQLException {
        Connection connection = null;
        try {
            String url = "jdbc:mysql://" + connectionDetails.get(0) + ":" + connectionDetails.get(1) + "/" + connectionDetails.get(2);
            String username = connectionDetails.get(3);
            String password = connectionDetails.get(4);

            // Establish a database connection
            connection = DriverManager.getConnection(url, username, password);
            // Create a SQL statement with parameter placeholders
            PreparedStatement statement = connection.prepareStatement(sql);

            // Execute the delete
            int rowsAffected = statement.executeUpdate();
            System.out.println("Deleted " + rowsAffected + " row(s).");
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

}
