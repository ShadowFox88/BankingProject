package org.banking;

import org.database.Database_Functions;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class BalanceOperations {
    static String sql_update = "UPDATE Accounts SET balance = ? WHERE uuid = ?";
    static ArrayList<String> connection_details = new ArrayList<>();
    // need to give details in order IP, Port, Database Name, Username, Password

    public static boolean deposit(BankAccount account, double amount) throws SQLException, IOException {
        return transfer(Objects.requireNonNull(BankAccount.getBankAccount(UUID.fromString("88888888-8888-8888-8888-888888888888"))), account, amount, "Deposit of £" + amount + ".");
    }

    public static boolean withdraw(BankAccount account, double amount) throws SQLException, IOException {
        return transfer(account, Objects.requireNonNull(BankAccount.getBankAccount(UUID.fromString("88888888-8888-8888-8888-888888888888"))), amount, "Withdrawal of £" + amount + ".");
    }

    public static boolean transfer(BankAccount from, BankAccount to, double amount, String note) throws SQLException, IOException {
        set_connection_details();
        if (!to.accountStatus.equals("Open") || !from.accountStatus.equals("Open") || from.accountIdentifier.equals(to.accountIdentifier) || amount <= 0) {
            return false;
        }
        if (from.balance - amount < 0) {
            return false;
        } else {
            Database_Functions.updateData(sql_update, to.balance + amount, to.accountIdentifier.toString());
            Database_Functions.updateData(sql_update, from.balance - amount, from.accountIdentifier.toString());
            Database_Functions.insertData("INSERT INTO Transactions (Sender, Receiver, Amount, SenderNote, Date) VALUES (?, ?, ?, ?, ?)", from.accountIdentifier.toString(), to.accountIdentifier.toString(), amount, note, LocalDateTime.now());
            to.balance += amount;
            from.balance -= amount;
            return true;
        }
    }

    public static void set_connection_details() throws IOException {
        HashMap<String, String> EnvVariables = EnvParser.parseEnvFile("C:/Users/Vahin/IdeaProjects/BankingProject/.env");
        if (connection_details.isEmpty()) {
            for (int i = 0; i < 5; i++) {
                connection_details.add("");
            }
        }
        connection_details.set(0, EnvVariables.get("DATABASE_IP"));
        connection_details.set(1, EnvVariables.get("DATABASE_PORT"));
        connection_details.set(2, EnvVariables.get("DATABASE_NAME"));
        connection_details.set(3, EnvVariables.get("DATABASE_USERNAME"));
        connection_details.set(4, EnvVariables.get("DATABASE_PASSWORD"));
    }

    public static void main(String[] args) throws IOException, SQLException {
        set_connection_details();

        transfer(Objects.requireNonNull(BankAccount.getBankAccount(UUID.fromString("40b57228-6722-4b71-9d6d-2e96549854dc"))), Objects.requireNonNull(BankAccount.getBankAccount(UUID.fromString("23535d8a-3759-4bf4-81a0-8999627a21cf"))), 0.01, null);
    }
}
