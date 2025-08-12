package com.revel8.ManikProject.model;

import java.math.BigDecimal;

public class TransactionRequest {
    private BigDecimal amount;

    // Getters and setters
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
