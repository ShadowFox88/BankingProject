package org.banking;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.database.Database_Functions;


public class BankAccount {

    static final String sql_insert = "INSERT INTO Accounts (uuid, name, address, balance, phone_number, account_type, date_of_birth, interest_rate, credit_limit, account_status, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    static double globalInterestRate = 5.25;

    // Should be 0 to begin with
    double balance = 0;
    // Name
    String name = "";
    // UUID type - should be random for everyone
    UUID accountIdentifier;
    // Address
    String address = "";
    // Phone Number
    String phoneNumber = "";
    // Savings, Credit, Debit
    String accountType = "";
    // Date Object
    Date dateOfBirth = new Date();
    // 0 - 100  - Currently 5.25%
    double interestRate = 0;
    // 0 - 5000
    int creditLimit = 0;
    // Open, Closed, Frozen
    String accountStatus = "";
    Connection connection = null;

    public BankAccount(String name, String address, String phoneNumber, String accountType, Date dateOfBirth, double interestRate, int creditLimit, String accountStatus) {
        this.name = name;
        this.balance = 0;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.accountType = accountType;
        this.dateOfBirth = dateOfBirth;
        this.interestRate = interestRate;
        this.creditLimit = creditLimit;
        this.accountStatus = accountStatus;
        this.accountIdentifier = UUID.randomUUID();
    }

    public static BankAccount createBankAccount(String name, String address, String phoneNumber, String accountType, Date dateOfBirth) throws IOException, SQLException {
        HashMap<String, String> EnvVariables = EnvParser.parseEnvFile("C:/Users/Vahin/IdeaProjects/BankingProject/.env");
        ArrayList<String> connection_details = new ArrayList<>();
        // need to give details in order IP, Port, Database Name, Username, Password
        connection_details.add(EnvVariables.get("DATABASE_IP"));
        connection_details.add(EnvVariables.get("DATABASE_PORT"));
        connection_details.add(EnvVariables.get("DATABASE_NAME"));
        connection_details.add(EnvVariables.get("DATABASE_USERNAME"));
        connection_details.add(EnvVariables.get("DATABASE_PASSWORD"));
        LocalDateTime now = LocalDateTime.now();

        switch (accountType) {
            case "Debit" -> {
                BankAccount bankAccount = new BankAccount(name, address, phoneNumber, "Debit", dateOfBirth, globalInterestRate, 0, "Open");
                // values need to be uuid, name, address, balance, phone_number, account_type, date_of_birth, interest_rate, credit_limit, account_status
                Database_Functions.insertData(connection_details, sql_insert, bankAccount.accountIdentifier.toString(), bankAccount.name, bankAccount.address, bankAccount.balance, bankAccount.phoneNumber, bankAccount.accountType, bankAccount.dateOfBirth, bankAccount.interestRate, bankAccount.creditLimit, bankAccount.accountStatus, now);
                Database_Functions.insertData(connection_details, "INSERT INTO Logs (Date, UserID, Action, Details) VALUES (?, ?, ?, ?)", now, bankAccount.accountIdentifier.toString(), "User Created", "User " + bankAccount.name + " has been created with UUID: " + bankAccount.accountIdentifier + ", and has created a " + bankAccount.accountType + " account.");
                return bankAccount;
            }
            case "Credit" -> {
                BankAccount bankAccount = new BankAccount(name, address, phoneNumber, "Credit", dateOfBirth, globalInterestRate, 5000, "Open");
                Database_Functions.insertData(connection_details, sql_insert, bankAccount.accountIdentifier.toString(), bankAccount.name, bankAccount.address, bankAccount.balance, bankAccount.phoneNumber, bankAccount.accountType, bankAccount.dateOfBirth, bankAccount.interestRate, bankAccount.creditLimit, bankAccount.accountStatus, now);
                Database_Functions.insertData(connection_details, "INSERT INTO Logs (Date, UserID, Action, Details) VALUES (?, ?, ?, ?)", now, bankAccount.accountIdentifier.toString(), "User Created", "User " + bankAccount.name + " has been created with UUID: " + bankAccount.accountIdentifier + ", and has created a " + bankAccount.accountType + " account.");
                return bankAccount;
            }
            case "Savings" -> {
                BankAccount bankAccount = new BankAccount(name, address, phoneNumber, "Savings", dateOfBirth, globalInterestRate, 0, "Open");
                Database_Functions.insertData(connection_details, sql_insert, bankAccount.accountIdentifier.toString(), bankAccount.name, bankAccount.address, bankAccount.balance, bankAccount.phoneNumber, bankAccount.accountType, bankAccount.dateOfBirth, bankAccount.interestRate, bankAccount.creditLimit, bankAccount.accountStatus, now);
                Database_Functions.insertData(connection_details, "INSERT INTO Logs (Date, UserID, Action, Details) VALUES (?, ?, ?, ?)", now, bankAccount.accountIdentifier.toString(), "User Created", "User " + bankAccount.name + " has been created with UUID: " + bankAccount.accountIdentifier + ", and has created a " + bankAccount.accountType + " account.");
                return bankAccount;
            }
            default -> {
                return null;
            }
        }
    }

    public static BankAccount getBankAccount(UUID uuid) throws SQLException, IOException {
        HashMap<String, String> EnvVariables = EnvParser.parseEnvFile("C:/Users/Vahin/IdeaProjects/BankingProject/.env");
        ArrayList<String> connection_details = new ArrayList<>();
        // need to give details in order IP, Port, Database Name, Username, Password
        connection_details.add(EnvVariables.get("DATABASE_IP"));
        connection_details.add(EnvVariables.get("DATABASE_PORT"));
        connection_details.add(EnvVariables.get("DATABASE_NAME"));
        connection_details.add(EnvVariables.get("DATABASE_USERNAME"));
        connection_details.add(EnvVariables.get("DATABASE_PASSWORD"));
        HashMap<Integer, HashMap<String, String>> data = Database_Functions.retrieveData(connection_details, "SELECT * FROM Accounts WHERE uuid = '" + uuid + "'");
        if (data.size() == 1) {
            HashMap<String, String> row = data.get(1);
            String name = row.get("name");
            String address = row.get("address");
            String phoneNumber = row.get("phone_number");
            String accountType = row.get("account_type");
            Date dateOfBirth = java.sql.Date.valueOf(LocalDate.parse(row.get("date_of_birth"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            double interestRate = Double.parseDouble(row.get("interest_rate"));
            int creditLimit = Integer.parseInt(row.get("credit_limit"));
            String accountStatus = row.get("account_status");
            BankAccount bankAccount = new BankAccount(name, address, phoneNumber, accountType, dateOfBirth, interestRate, creditLimit, accountStatus);
            bankAccount.accountIdentifier = uuid;
            bankAccount.balance = Double.parseDouble(row.get("balance"));
            return bankAccount;
        }
        return null;
    }

    public static void main(String[] args) throws IOException, SQLException {
        String string = "September 21 2008";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd yyyy", Locale.ENGLISH);
        Date date = java.sql.Date.valueOf(LocalDate.parse(string, formatter));
        BankAccount bankAccount = createBankAccount("Vahin Mehra", "1234 Testing Lane", "1234", "Debit", date);
        assert bankAccount != null;
        System.out.println(bankAccount.accountType);
        System.out.println(bankAccount.accountIdentifier);
//        try {
//            System.out.println(Objects.requireNonNull(getBankAccount(UUID.fromString("23535d8a-3759-4bf4-81a0-8999627a21cf"))).dateOfBirth);
//        } catch (NullPointerException e) {
//            System.out.println("Account not found");
//        }

    }

}