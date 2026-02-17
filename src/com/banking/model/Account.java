package com.banking.model;

/**
 * Account Model Class
 * Simple POJO (Plain Old Java Object) to represent a bank account
 */
public class Account {
    private int accountNumber;
    private String holderName;
    private double balance;
    private String email;
    private String accountType; // "Savings" or "Current"

    // Default constructor
    public Account() {
    }

    // Parameterized constructor
    public Account(int accountNumber, String holderName, double balance, String email, String accountType) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
        this.email = email;
        this.accountType = accountType;
    }

    // Getters and Setters
    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", holderName='" + holderName + '\'' +
                ", balance=" + balance +
                ", email='" + email + '\'' +
                ", accountType='" + accountType + '\'' +
                '}';
    }
}
