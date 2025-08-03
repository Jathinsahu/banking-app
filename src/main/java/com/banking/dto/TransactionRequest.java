package com.banking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * DTO for transaction requests (credit, debit, transfer)
 */
public class TransactionRequest {
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    
    // For transfer operations - target username or user ID
    private String targetUsername;
    
    // Optional description for the transaction
    private String description;
    
    // Constructors
    public TransactionRequest() {}
    
    public TransactionRequest(BigDecimal amount) {
        this.amount = amount;
    }
    
    public TransactionRequest(BigDecimal amount, String targetUsername, String description) {
        this.amount = amount;
        this.targetUsername = targetUsername;
        this.description = description;
    }
    
    // Getters and Setters
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getTargetUsername() {
        return targetUsername;
    }
    
    public void setTargetUsername(String targetUsername) {
        this.targetUsername = targetUsername;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}