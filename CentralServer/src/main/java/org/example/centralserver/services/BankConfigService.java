package org.example.centralserver.services;

import org.example.centralserver.entities.config.*;
import org.example.centralserver.repo.mongo.BankConfigRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankConfigService {

    @Autowired
    private BankConfigRepo bankConfigRepository;


    public BankConfig saveBankConfig(BankConfig bankConfig) {
        return bankConfigRepository.save(bankConfig);
    }

    public List<BankConfig> getAllBankConfig() {
        return bankConfigRepository.findAll();
    }
}
