package com.banking.entity;

/**
 * Enum representing different types of transactions in the banking system
 */
public enum TransactionType {
    CREDIT,    // Money added to an account
    DEBIT,     // Money removed from an account
    TRANSFER   // Money transferred between accounts
}