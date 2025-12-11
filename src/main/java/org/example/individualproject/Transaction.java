package org.example.individualproject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private int id;
    private String type;
    private double amount;
    private String description;
    private LocalDate date;
    private double runningBalance; // Нове поле

    public Transaction(int id, String type, double amount, String description, String dateString) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = LocalDate.parse(dateString);
    }

    // Getters
    public int getId() { return id; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }

    public String getDateFormatted() {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    // Getter and Setter для нового поля
    public double getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(double runningBalance) {
        this.runningBalance = runningBalance;
    }
}
