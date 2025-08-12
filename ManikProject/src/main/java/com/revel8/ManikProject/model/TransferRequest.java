package com.revel8.ManikProject.model;

import java.math.BigDecimal;

public class TransferRequest {
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;

    // Getters and setters
    public String getFromAccount() { return fromAccount; }
    public void setFromAccount(String fromAccount) { this.fromAccount = fromAccount; }
    public String getToAccount() { return toAccount; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}

