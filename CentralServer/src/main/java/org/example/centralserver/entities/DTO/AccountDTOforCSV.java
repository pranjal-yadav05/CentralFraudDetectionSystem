package org.example.centralserver.entities.DTO;

public class AccountDTOforCSV {
    private String accountNumber;
    private boolean is_suspicious;

    public AccountDTOforCSV(String accountNumber, boolean is_suspicious) {
        this.accountNumber = accountNumber;
        this.is_suspicious = is_suspicious;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public boolean isIs_suspicious() {
        return is_suspicious;
    }
}