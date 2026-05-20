package model;

import java.time.LocalDate;

public class Expense extends cashflow.model.Transaction {

    private ExpenseCategory category;
    private ExpenseSubCategory subCategory;

    public Expense(double amount,
                   LocalDate date,
                   String description,
                   ExpenseCategory category,
                   ExpenseSubCategory subCategory) {

        super(amount, date, description);

        this.category = category;
        this.subCategory = subCategory;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public ExpenseSubCategory getSubCategory() {
        return subCategory;
    }

    @Override
    public String getTransactionType() {
        return "EXPENSE";
    }

    @Override
    public String getDetail() { return category + " - " + subCategory;
    }
}