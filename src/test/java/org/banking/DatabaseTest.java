package org.banking;

import org.database.Database_Functions;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    @Test
    public void DatabaseConnectionTest() throws IOException {

        HashMap<String, String> EnvVariables = EnvParser.parseEnvFile("C:/Users/Vahin/IdeaProjects/BankingProject/.env");

        // Connection parameters
        String url = "jdbc:mysql://" + EnvVariables.get("DATABASE_IP") + ":" + EnvVariables.get("DATABASE_PORT") + "/" + EnvVariables.get("DATABASE_NAME");
        String username = EnvVariables.get("DATABASE_USERNAME");
        String password = EnvVariables.get("DATABASE_PASSWORD");

        // Establish a database connection
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            assertTrue(connection.isValid(5));

        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        } finally {
            // Close the connection when you're done
            if (connection != null) {
                try {
                    connection.close();
                    assertFalse(connection.isValid(5));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void DatabaseGetterTest() throws IOException, SQLException {
        HashMap<String, String> EnvVariables = EnvParser.parseEnvFile("C:/Users/Vahin/IdeaProjects/BankingProject/.env");

        // Requires value to have been set already to the "TEST_VALUE" key from the .env file

        HashMap<Integer, HashMap<String, String>> Results = Database_Functions.retrieveData("SELECT * FROM test_connection");

        assertEquals(Results.get(1).get("value"), EnvVariables.get("TEST_VALUE"));
    }

    @Test
    public void DatabaseSetterTest() throws SQLException {
        UUID uuid = UUID.randomUUID();

        Database_Functions.insertData("INSERT INTO test_connection (value) VALUES (?)", uuid.toString());

        HashMap<Integer, HashMap<String, String>> Results = Database_Functions.retrieveData("SELECT * FROM test_connection");

        assertEquals(Results.get(2).get("value"), uuid.toString());

        Database_Functions.deleteData("DELETE FROM test_connection WHERE value = '" + uuid + "'");
    }
}

