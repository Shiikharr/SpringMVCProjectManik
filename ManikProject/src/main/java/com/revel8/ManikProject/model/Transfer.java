package com.revel8.ManikProject.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transfer {
    private final Long id;
    private final String fromAccount;
    private final String toAccount;
    private final BigDecimal amount;
    private final LocalDateTime timestamp;

    public Transfer(Long id, String fromAccount, String toAccount, BigDecimal amount) {
        this.id = id;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public String getFromAccount() { return fromAccount; }
    public String getToAccount() { return toAccount; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
