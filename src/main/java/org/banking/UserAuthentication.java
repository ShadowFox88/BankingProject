package org.banking;

import org.database.Database_Functions;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

public class UserAuthentication {

    public static void login(String username, String password) throws SQLException, NoSuchAlgorithmException {
        HashMap<Integer, HashMap<String, String>> data = Database_Functions.retrieveData("SELECT * FROM login_info WHERE username = '" + username + "'");

        if (data.size() == 1) {
            String password_hashed = data.get(1).get("password_hashed");
            String salt = data.get(1).get("salt");

            String saltedPassword = password + salt;

            String hashedPassword = hashPassword(password, salt).get("hashed_password");
            if (hashedPassword.equals(password_hashed)) {
                System.out.println("Login Successful");
            } else {
                System.out.println("Login Failed");
            }
        }
    }

    public static boolean register(String username, String password, UUID accountIdentifier) throws SQLException, NoSuchAlgorithmException {
        HashMap<Integer, HashMap<String, String>> value = Database_Functions.retrieveData(String.format("SELECT * FROM login_info WHERE username = '%s' AND uuid = '%s'", username, accountIdentifier.toString()));
        if (value.size() == 0) {

            String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+";

            Random random = new Random();

            StringBuilder salt = new StringBuilder();

            for (int i = 0; i <= 20; i++) {
                salt.append(characters.charAt(random.nextInt(characters.length())));
            }

            HashMap<String, String> password_details = hashPassword(password, salt.toString());

            try {
                Database_Functions.insertData("INSERT INTO login_info (username, password_hashed, salt, uuid) VALUES (?, ?, ?, ?)", username, password_details.get("hashed_password"), password_details.get("salt"), accountIdentifier.toString());
            } catch (SQLIntegrityConstraintViolationException e) {
                e.printStackTrace();
                System.err.println("Duplicate or Invalid UUID/Username.");
                return false;
            }
            return true;
        }
        return false;
    }

    private static HashMap<String, String> hashPassword(String password, String salt) throws NoSuchAlgorithmException {

        String saltedPassword = password + salt;

        // Generate password digest
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashed_password_bytes = md.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));

        BigInteger number = new BigInteger(1, hashed_password_bytes);
        // Convert message digest into hex value
        StringBuilder hashed_password = new StringBuilder(number.toString(16));
        // Ensure that the hex value is 64 characters long by adding 0s
        while (hashed_password.length() < 64)
        {
            hashed_password.insert(0, '0');
        }


        // Create the HashMap to return
        HashMap<String, String> return_value = new HashMap<>();
        return_value.put("salt", salt.toString());
        return_value.put("hashed_password", hashed_password.toString());
        return return_value;
    }

    public static void main(String[] args) throws SQLException, NoSuchAlgorithmException {
        // System.out.println(register("test", "test", UUID.fromString("88888888-8888-8888-8888-888888888888")));
        login("test", "test");
    }
}
