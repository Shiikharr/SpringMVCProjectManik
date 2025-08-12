package com.revel8.ManikProject.service;

import com.revel8.ManikProject.model.Account;
import com.revel8.ManikProject.model.Transfer;
import com.revel8.ManikProject.model.TransferRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BankingService {
    // HashMap to store accounts - accountNumber -> Account
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final AtomicLong accountIdCounter = new AtomicLong(1);
    private final AtomicLong transferIdCounter = new AtomicLong(1);

    public Account createAccount(String accountNumber, BigDecimal initialBalance) {
        if (accounts.containsKey(accountNumber)) {
            throw new IllegalArgumentException("Account already exists");
        }

        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        Account account = new Account(accountIdCounter.getAndIncrement(), accountNumber, initialBalance);
        accounts.put(accountNumber, account);
        return account;
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public Account getAccount(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        return account;
    }

    // Deposit money to account
    public Account deposit(String accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Account account = getAccount(accountNumber);
        synchronized (account) { // Thread safety for balance updates
            account.setBalance(account.getBalance().add(amount));
        }
        return account;
    }

    // Withdraw money from account
    public Account withdraw(String accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Account account = getAccount(accountNumber);
        synchronized (account) { // Thread safety for balance updates
            if (account.getBalance().compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient funds");
            }
            account.setBalance(account.getBalance().subtract(amount));
        }
        return account;
    }

    // Transfer money between accounts
    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("Cannot transfer to same account");
        }

        Account fromAccount = getAccount(fromAccountNumber);
        Account toAccount = getAccount(toAccountNumber);

        // Use synchronized block to ensure atomicity of transfer
        Object lock1 = fromAccountNumber.compareTo(toAccountNumber) < 0 ? fromAccount : toAccount;
        Object lock2 = fromAccountNumber.compareTo(toAccountNumber) < 0 ? toAccount : fromAccount;

        synchronized (lock1) {
            synchronized (lock2) {
                if (fromAccount.getBalance().compareTo(amount) < 0) {
                    throw new IllegalArgumentException("Insufficient funds");
                }

                // Perform the transfer
                fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
                toAccount.setBalance(toAccount.getBalance().add(amount));

                // Create and store transfer record
                Transfer transfer = new Transfer(
                        transferIdCounter.getAndIncrement(),
                        fromAccountNumber,
                        toAccountNumber,
                        amount
                );

                fromAccount.addOutgoingTransfer(transfer);
            }
        }
    }
}

