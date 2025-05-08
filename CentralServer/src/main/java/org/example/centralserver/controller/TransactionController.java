package org.example.centralserver.controller;



import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.centralserver.entities.Transection;
import org.example.centralserver.repo.mongo.AccountRepo;
import org.example.centralserver.repo.mongo.TransectionRepo;
import org.example.centralserver.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransectionRepo transectionRepo;

    @Autowired
    private AccountRepo accountRepo;


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping
    public void fetchTransactions() throws Exception {
        System.out.println("Fetching transactions");

        transectionRepo.deleteAll();
        accountRepo.deleteAll();

        // --- CLEAN Redis ---
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            System.out.println("Deleted Redis keys: " + keys);
        }


        transactionService.processTransactions();
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Transection>> getAllTransactions() throws JsonProcessingException {
        List<Transection>transectionList=transectionRepo.findAll();
        return ResponseEntity.ok(transectionList);
    }



}
