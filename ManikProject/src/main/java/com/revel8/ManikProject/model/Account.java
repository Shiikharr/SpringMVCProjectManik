package com.revel8.ManikProject.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Account {
    private final Long id;
    private final String accountNumber;
    private BigDecimal balance;
    private final LocalDateTime createdAt;
    private final Map<Long, Transfer> outgoingTransfers; // HashMap to store last 50 transfers

    public Account(Long id, String accountNumber, BigDecimal initialBalance) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.createdAt = LocalDateTime.now();
        this.outgoingTransfers = new LinkedHashMap<>(); // LinkedHashMap maintains insertion order
    }

    // Getters and setters
    public Long getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    // Add outgoing transfer (keep only last 50)
    public void addOutgoingTransfer(Transfer transfer) {
        outgoingTransfers.put(transfer.getId(), transfer);
        // Remove oldest transfers if we exceed 50
        if (outgoingTransfers.size() > 50) {
            Long oldestKey = outgoingTransfers.keySet().iterator().next();
            outgoingTransfers.remove(oldestKey);
        }
    }

    // Get all outgoing transfers
    public List<Transfer> getOutgoingTransfers() {
        return new ArrayList<>(outgoingTransfers.values());
    }
}
