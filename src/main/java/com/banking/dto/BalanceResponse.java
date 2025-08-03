package com.banking.dto;

import java.math.BigDecimal;

/**
 * DTO for account balance response
 */
public class BalanceResponse {
    
    private String username;
    private BigDecimal balance;
    
    // Constructors
    public BalanceResponse() {}
    
    public BalanceResponse(String username, BigDecimal balance) {
        this.username = username;
        this.balance = balance;
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}