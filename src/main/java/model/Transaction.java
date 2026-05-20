package cashflow.model;

import java.time.LocalDate;

public abstract class Transaction {

    private static int counter = 1;
    private int id;
    private double amount;
    private LocalDate date;
    private String description;

    public Transaction(double amount,
                       LocalDate date,
                       String description) {

        this.id = counter++;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public int getId() {
        return id;
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