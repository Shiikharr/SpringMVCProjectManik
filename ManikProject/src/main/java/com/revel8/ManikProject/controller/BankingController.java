package com.revel8.ManikProject.controller;

import com.revel8.ManikProject.*;
import com.revel8.ManikProject.model.Account;
import com.revel8.ManikProject.model.CreateAccountRequest;
import com.revel8.ManikProject.model.TransactionRequest;
import com.revel8.ManikProject.model.TransferRequest;
import com.revel8.ManikProject.service.BankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

// Simple REST controller with just basic endpoints
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow all origins for testing
class BankingController {
    private final BankingService bankingService;

    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    // Test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Banking API is working!");
    }

    // Create account endpoint
    @PostMapping("/accounts")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest request) {
        try {
            // Validate account number
            if (request.getAccountNumber() == null || request.getAccountNumber().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Account number is required"));
            }

            // Validate initial balance
            BigDecimal initialBalance = request.getInitialBalance();
            if (initialBalance == null) {
                initialBalance = BigDecimal.ZERO;
            }

            if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Initial balance cannot be negative"));
            }

            Account account = bankingService.createAccount(request.getAccountNumber().trim(), initialBalance);
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get all accounts endpoint
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(bankingService.getAllAccounts());
    }

    // Get single account endpoint
    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<?> getAccount(@PathVariable String accountNumber) {
        try {
            Account account = bankingService.getAccount(accountNumber);
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Deposit money endpoint
    @PostMapping("/accounts/{accountNumber}/deposit")
    public ResponseEntity<?> deposit(@PathVariable String accountNumber, @RequestBody TransactionRequest request) {
        try {
            Account account = bankingService.deposit(accountNumber, request.getAmount());
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Withdraw money endpoint
    @PostMapping("/accounts/{accountNumber}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable String accountNumber, @RequestBody TransactionRequest request) {
        try {
            Account account = bankingService.withdraw(accountNumber, request.getAmount());
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Transfer money endpoint
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        try {
            bankingService.transfer(request.getFromAccount(), request.getToAccount(), request.getAmount());
            return ResponseEntity.ok(Map.of("message", "Transfer successful"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get outgoing transfers for an account
    @GetMapping("/accounts/{accountNumber}/transfers")
    public ResponseEntity<?> getOutgoingTransfers(@PathVariable String accountNumber) {
        try {
            Account account = bankingService.getAccount(accountNumber);
            return ResponseEntity.ok(account.getOutgoingTransfers());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}