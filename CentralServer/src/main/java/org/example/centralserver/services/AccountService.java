package org.example.centralserver.services;

import org.example.centralserver.entities.Account;
import org.example.centralserver.repo.mongo.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    public AccountRepo accountRepo;

    public AccountService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    public Account addaccount(Account account) {
        return accountRepo.save(account);
    }
    public Account getaccount(String id) {
        return accountRepo.findById(id).orElse(null);
    }
    public List<Account> getaccounts() { return accountRepo.findAll(); }

}
