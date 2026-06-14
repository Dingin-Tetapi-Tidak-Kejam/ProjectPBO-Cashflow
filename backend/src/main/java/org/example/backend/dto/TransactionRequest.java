package org.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class TransactionRequest {

    @NotBlank(message = "Tipe transaksi wajib diisi")
    private String transactionType;

    @NotNull(message = "Amount wajib diisi")
    @Positive(message = "Amount harus lebih besar dari 0")
    private Double amount;

    @NotNull(message = "Tanggal wajib diisi")
    private LocalDate date;

    private String description;
    private String detail;

    public TransactionRequest() {
    }

    public TransactionRequest(String transactionType, Double amount, LocalDate date, String description, String detail) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.detail = detail;
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