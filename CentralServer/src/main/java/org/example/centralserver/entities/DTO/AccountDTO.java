package org.example.centralserver.entities.DTO;

import java.time.LocalDateTime;

public class AccountDTO {

    private String accountNumber;
    private double balance;
    private double frequency;
    private LocalDateTime lastTransaction;
    private double suspiciousScore;
    public AccountDTO() {}


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public LocalDateTime getLastTransaction() {
        return lastTransaction;
    }

    public void setLastTransaction(LocalDateTime lastTransaction) {
        this.lastTransaction = lastTransaction;
    }

    public double getSuspiciousScore() {
        return suspiciousScore;
    }
    public void setSuspiciousScore(double suspiciousScore) {
        this.suspiciousScore = suspiciousScore;
    }
}
