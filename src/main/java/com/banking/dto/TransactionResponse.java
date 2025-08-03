package com.banking.dto;

import com.banking.entity.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for transaction response
 */
public class TransactionResponse {
    
    private Long id;
    private String senderUsername;
    private String receiverUsername;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private TransactionType type;
    private String description;
    
    // Constructors
    public TransactionResponse() {}
    
    public TransactionResponse(Long id, String senderUsername, String receiverUsername, 
                             BigDecimal amount, LocalDateTime timestamp, 
                             TransactionType type, String description) {
        this.id = id;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.amount = amount;
        this.timestamp = timestamp;
        this.type = type;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getSenderUsername() {
        return senderUsername;
    }
    
    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }
    
    public String getReceiverUsername() {
        return receiverUsername;
    }
    
    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public void setType(TransactionType type) {
        this.type = type;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}