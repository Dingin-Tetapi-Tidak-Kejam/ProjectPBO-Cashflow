package org.example.backend.dto;

import java.time.LocalDate;

public class TransactionResponse {

    private Long id;
    private String transactionType;
    private Double amount;
    private LocalDate date;
    private String description;
    private String detail;

    public TransactionResponse() {
    }

    public TransactionResponse(Long id, String transactionType, Double amount, LocalDate date, String description, String detail) {
        this.id = id;
        this.transactionType = transactionType;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.detail = detail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}