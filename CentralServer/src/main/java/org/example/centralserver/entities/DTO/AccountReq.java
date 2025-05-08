package org.example.centralserver.entities.DTO;

import java.util.List;

public class AccountReq {
    private List<AccountDTOforCSV> accounts;

    public AccountReq(List<AccountDTOforCSV> accounts) {
        this.accounts = accounts;
    }

    public List<AccountDTOforCSV> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountDTOforCSV> accounts) {
        this.accounts = accounts;
    }
}
