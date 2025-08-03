package com.banking.service;

import com.banking.dto.BalanceResponse;
import com.banking.dto.TransactionRequest;
import com.banking.dto.TransactionResponse;
import com.banking.entity.Transaction;
import com.banking.entity.TransactionType;
import com.banking.entity.User;
import com.banking.repository.TransactionRepository;
import com.banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for banking operations
 */
@Service
@Transactional
public class BankingService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    /**
     * Get current authenticated user
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    /**
     * Get account balance for current user
     */
    public BalanceResponse getBalance() {
        User user = getCurrentUser();
        return new BalanceResponse(user.getUsername(), user.getBalance());
    }
    
    /**
     * Credit money to current user's account
     */
    public TransactionResponse creditMoney(TransactionRequest request) {
        User user = getCurrentUser();
        
        // Update user balance
        user.setBalance(user.getBalance().add(request.getAmount()));
        userRepository.save(user);
        
        // Create transaction record
        Transaction transaction = new Transaction(
                null, // No sender for credit
                user,
                request.getAmount(),
                TransactionType.CREDIT,
                request.getDescription() != null ? request.getDescription() : "Account credited"
        );
        
        transaction = transactionRepository.save(transaction);
        
        return convertToTransactionResponse(transaction);
    }
    
    /**
     * Debit money from current user's account
     */
    public TransactionResponse debitMoney(TransactionRequest request) {
        User user = getCurrentUser();
        
        // Check if user has sufficient balance
        if (user.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        
        // Update user balance
        user.setBalance(user.getBalance().subtract(request.getAmount()));
        userRepository.save(user);
        
        // Create transaction record
        Transaction transaction = new Transaction(
                user,
                null, // No receiver for debit
                request.getAmount(),
                TransactionType.DEBIT,
                request.getDescription() != null ? request.getDescription() : "Account debited"
        );
        
        transaction = transactionRepository.save(transaction);
        
        return convertToTransactionResponse(transaction);
    }
    
    /**
     * Transfer money to another user
     */
    public TransactionResponse transferMoney(TransactionRequest request) {
        User sender = getCurrentUser();
        
        // Find receiver by username
        User receiver = userRepository.findByUsername(request.getTargetUsername())
                .orElseThrow(() -> new RuntimeException("Target user not found"));
        
        // Check if sender has sufficient balance
        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        
        // Check if sender is not transferring to themselves
        if (sender.getId().equals(receiver.getId())) {
            throw new RuntimeException("Cannot transfer money to yourself");
        }
        
        // Update balances
        sender.setBalance(sender.getBalance().subtract(request.getAmount()));
        receiver.setBalance(receiver.getBalance().add(request.getAmount()));
        
        userRepository.save(sender);
        userRepository.save(receiver);
        
        // Create transaction record
        Transaction transaction = new Transaction(
                sender,
                receiver,
                request.getAmount(),
                TransactionType.TRANSFER,
                request.getDescription() != null ? request.getDescription() : 
                    "Transfer from " + sender.getUsername() + " to " + receiver.getUsername()
        );
        
        transaction = transactionRepository.save(transaction);
        
        return convertToTransactionResponse(transaction);
    }
    
    /**
     * Get transaction history for current user
     */
    public List<TransactionResponse> getTransactionHistory() {
        User user = getCurrentUser();
        List<Transaction> transactions = transactionRepository.findAllByUser(user);
        
        return transactions.stream()
                .map(this::convertToTransactionResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get transaction history for current user with date range filter
     */
    public List<TransactionResponse> getTransactionHistory(LocalDate fromDate, LocalDate toDate) {
        User user = getCurrentUser();
        
        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atTime(LocalTime.MAX);
        
        List<Transaction> transactions = transactionRepository.findByUserAndDateRange(user, fromDateTime, toDateTime);
        
        return transactions.stream()
                .map(this::convertToTransactionResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert Transaction entity to TransactionResponse DTO
     */
    private TransactionResponse convertToTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getSender() != null ? transaction.getSender().getUsername() : null,
                transaction.getReceiver() != null ? transaction.getReceiver().getUsername() : null,
                transaction.getAmount(),
                transaction.getTimestamp(),
                transaction.getType(),
                transaction.getDescription()
        );
    }
}