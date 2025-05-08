package org.example.centralserver.controller;

import org.example.centralserver.entities.config.BankConfig;
import org.example.centralserver.services.BankConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
public class BankConfigController {


    @Autowired
    private BankConfigService bankConfigService;

    @PostMapping("/bank")
    public ResponseEntity<BankConfig> createBankConfig(@RequestBody BankConfig bankConfig) {
        return ResponseEntity.ok(bankConfigService.saveBankConfig(bankConfig));
    }

}
