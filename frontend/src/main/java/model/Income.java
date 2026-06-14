package model;

import java.time.LocalDate;

public class Income extends Transaction {

    private IncomeSource source;

    public Income(double amount, LocalDate date, String description, IncomeSource source) {
        super(amount, date, description);
        this.source = source;
    }

    public IncomeSource getSource() {
        return source;
    }

    @Override
    public String getTransactionType() {
        return "INCOME";
    }

    @Override
    public String getDetail() {
        return source.toString();
    }
}