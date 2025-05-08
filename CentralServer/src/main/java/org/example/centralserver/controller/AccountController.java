package org.example.centralserver.controller;

import org.example.centralserver.entities.Account;
import org.example.centralserver.entities.DTO.AccountDTO;
import org.example.centralserver.repo.mongo.AccountRepo;
import org.example.centralserver.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")

public class AccountController {

    @Autowired
    AccountService accountService;
    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAccounts() {


        List<AccountDTO> accountDTOS=new ArrayList<>();

        List<Account>accounts=accountRepo.findAll();

        for(Account account:accounts) {

            AccountDTO accountDTO=new AccountDTO();
            accountDTO.setAccountNumber(account.getAccountNumber());
            accountDTO.setFrequency(account.getFreq());
            accountDTO.setSuspiciousScore(account.getSuspiciousScore());
            accountDTO.setLastTransaction(account.getLastTransaction());

            accountDTOS.add(accountDTO);

        }

        return ResponseEntity.ok(accountDTOS);


    }

}
