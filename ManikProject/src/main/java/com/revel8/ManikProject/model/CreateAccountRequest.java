package com.revel8.ManikProject.model;

import java.math.BigDecimal;

public class CreateAccountRequest {
    private String accountNumber;
    private BigDecimal initialBalance = BigDecimal.ZERO;

    // Getters and setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public BigDecimal getInitialBalance() { return initialBalance; }
    public void setInitialBalance(BigDecimal initialBalance) { this.initialBalance = initialBalance; }
}

