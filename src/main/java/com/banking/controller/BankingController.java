package com.banking.controller;

import com.banking.dto.ApiResponse;
import com.banking.dto.BalanceResponse;
import com.banking.dto.TransactionRequest;
import com.banking.dto.TransactionResponse;
import com.banking.service.BankingService;
import com.banking.util.CsvExportUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for banking operations
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/account")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class BankingController {
    
    @Autowired
    private BankingService bankingService;
    
    @Autowired
    private CsvExportUtil csvExportUtil;
    
    /**
     * Get account balance
     */
    @GetMapping("/balance")
    public ResponseEntity<?> getBalance() {
        try {
            BalanceResponse balance = bankingService.getBalance();
            return ResponseEntity.ok(ApiResponse.success("Balance retrieved successfully", balance));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to get balance: " + e.getMessage()));
        }
    }
    
    /**
     * Credit money to account
     */
    @PostMapping("/credit")
    public ResponseEntity<?> creditMoney(@Valid @RequestBody TransactionRequest request) {
        try {
            TransactionResponse transaction = bankingService.creditMoney(request);
            return ResponseEntity.ok(ApiResponse.success("Money credited successfully", transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Credit failed: " + e.getMessage()));
        }
    }
    
    /**
     * Debit money from account
     */
    @PostMapping("/debit")
    public ResponseEntity<?> debitMoney(@Valid @RequestBody TransactionRequest request) {
        try {
            TransactionResponse transaction = bankingService.debitMoney(request);
            return ResponseEntity.ok(ApiResponse.success("Money debited successfully", transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Debit failed: " + e.getMessage()));
        }
    }
    
    /**
     * Transfer money to another user
     */
    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(@Valid @RequestBody TransactionRequest request) {
        try {
            if (request.getTargetUsername() == null || request.getTargetUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Target username is required for transfer"));
            }
            
            TransactionResponse transaction = bankingService.transferMoney(request);
            return ResponseEntity.ok(ApiResponse.success("Money transferred successfully", transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Transfer failed: " + e.getMessage()));
        }
    }
    
    /**
     * Get transaction history
     */
    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        try {
            List<TransactionResponse> transactions;
            
            if (from != null && to != null) {
                transactions = bankingService.getTransactionHistory(from, to);
            } else {
                transactions = bankingService.getTransactionHistory();
            }
            
            return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", transactions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to get transactions: " + e.getMessage()));
        }
    }
    
    /**
     * Download transaction history as CSV
     */
    @GetMapping("/transactions/download")
    public ResponseEntity<?> downloadTransactions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        try {
            List<TransactionResponse> transactions;
            
            if (from != null && to != null) {
                transactions = bankingService.getTransactionHistory(from, to);
            } else {
                transactions = bankingService.getTransactionHistory();
            }
            
            String csvContent = csvExportUtil.exportTransactionsToCsv(transactions);
            
            String filename = "transactions_" + LocalDate.now() + ".csv";
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.parseMediaType("application/csv"))
                    .body(csvContent);
                    
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to download transactions: " + e.getMessage()));
        }
    }
}