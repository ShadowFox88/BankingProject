package org.banking;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;


public class BankAccount {
    double globalInterestRate = 5.25;

    // Should be 0 to begin with
    int balance = 0;
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

    public BankAccount createBankAccount(String name, String address, String phoneNumber, String accountType, Date dateOfBirth) {
        return switch (accountType) {
            case "Debit" ->
                    new BankAccount(name, address, phoneNumber, "Debit", dateOfBirth, globalInterestRate, 0, "Open");
            case "Credit" ->
                    new BankAccount(name, address, phoneNumber, "Credit", dateOfBirth, globalInterestRate, 5000, "Open");
            case "Savings" ->
                    new BankAccount(name, address, phoneNumber, "Savings", dateOfBirth, globalInterestRate, 0, "Open");
            default -> null;
        };
    }

    public static void main(String[] args) throws IOException {
        HashMap<String, String> environment_variables = EnvParser.parseEnvFile("C:/Users/Vahin/IdeaProjects/BankingProject/.env");
        System.out.println(environment_variables);
        System.out.println("Hello World!");
    }

}