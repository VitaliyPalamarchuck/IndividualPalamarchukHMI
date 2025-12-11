package org.example.individualproject;

public class User {
    private int id;
    private String username;
    private String name;
    private double balance;

    public User(int id, String username, String name, double balance) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.balance = balance;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    // Setters
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setName(String name) {
        this.name = name;
    }
}
