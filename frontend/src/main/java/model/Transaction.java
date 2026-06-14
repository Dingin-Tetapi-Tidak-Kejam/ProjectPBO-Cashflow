package model;

import java.time.LocalDate;

public abstract class Transaction {

    private double amount;
    private LocalDate date;
    private String description;

    public Transaction(double amount, LocalDate date, String description) {
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public abstract String getTransactionType();

    public abstract String getDetail();
}