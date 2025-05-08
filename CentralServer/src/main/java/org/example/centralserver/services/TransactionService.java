package org.example.centralserver.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.centralserver.entities.Account;
import org.example.centralserver.entities.DTO.AccountDTO;
import org.example.centralserver.entities.DTO.AccountDTOforCSV;
import org.example.centralserver.entities.DTO.AccountReq;
import org.example.centralserver.entities.Transection;
import org.example.centralserver.entities.config.BankConfig;
import org.example.centralserver.entities.neo4j.AccountNode;
import org.example.centralserver.entities.neo4j.TransactionRelationship;
import org.example.centralserver.repo.mongo.AccountRepo;
import org.example.centralserver.repo.mongo.TransectionRepo;
import org.example.centralserver.repo.neo4j.AccountNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static java.lang.Thread.sleep;

@Service
public class TransactionService {

    private static final int BATCH_SIZE = 10000;



    @Autowired
    TransactionProcessorService transactionProcessorService;
    @Autowired
    BankConfigService bankConfigService;
    @Autowired
    TransformData transformData;

    @Autowired
    RestTemplate restTemplate;


    @Value("${FLASKURI}")
    private String uri;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    RedisService redisService;

    List<CompletableFuture<Void>> futures = new ArrayList<>();
    @Autowired
    private TransectionRepo transectionRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private AccountNodeRepository accountNodeRepo;
    @Autowired
    private AccountService accountService;


    public void processTransactions() throws Exception {
        System.out.println("Fetching transactions from bank API...");

        Instant startTime = Instant.now();
        // Fetch transactions from the bank's API

        List<BankConfig> banks = bankConfigService.getAllBankConfig();
        List<List<Transection>>allTransactions = new ArrayList<>();

        banks.forEach(bankConfig -> {
            List<Transection> transectionList = transformData.convertAndProcessData(bankConfig);
            if(Objects.equals(bankConfig.getDatabaseStructure(), "NOSQL")) {
                allTransactions.add(transectionList);
            }

        });


        for (List<Transection> transectionList : allTransactions) {
            int totalTransactions = transectionList.size();
            int processedCount = 0;

            //replace by with chunk

            while (processedCount < totalTransactions) {
                List<CompletableFuture<Void>> batchFutures = new ArrayList<>();

                // Process one batch
                int endIndex = Math.min(processedCount + BATCH_SIZE, totalTransactions);
                for (int i = processedCount; i < endIndex; i++) {
                    CompletableFuture<Void> future = transactionProcessorService.processTransactionAsync(transectionList.get(i), banks.get(0).getBankId());
                    batchFutures.add(future);
                }

                // Wait for batch completion
                CompletableFuture.allOf(batchFutures.toArray(new CompletableFuture[0])).join();
                processedCount = endIndex;
            }



        }


         loadIntoDb();


        if(!postDataToFlask(accountService.getaccounts()))
        {
            throw new Exception("Error in generating csv for test");
        }
        else {
            System.out.println("CSV generated");
        }



    }

    private void loadIntoDb() {

        // Move all accounts from Redis to accountRepo
        Set<Object> accountKeys = redisService.getSetMembers("accounts");
        if (accountKeys != null) {
            for (Object accountKey : accountKeys) {
                Account account = redisService.getObject(accountKey.toString(), Account.class);


//                Optional<AccountNode> existingAccountNode = accountNodeRepo.findByAccountNumber(account.getAccountNumber());
//
//
//                AccountNode accountNode;

//                if (existingAccountNode.isPresent()) {
//                    // If node exists, update the existing node
//                    accountNode = existingAccountNode.get();
//
//                    // Update all fields of the existing node
//                    accountNode.setFreq((int)account.getFreq());
//                    accountNode.setRegularIntervalTransaction(account.getRegularIntervelTransection());
//                    accountNode.setSuspicious(account.getSuspicious());
//                } else {
//                    // If no existing node, create a new one
//                    accountNode = new AccountNode();
//                    accountNode.setAccountNumber(account.getAccountNumber());
//                    accountNode.setFreq((int)account.getFreq());
//                    accountNode.setRegularIntervalTransaction(account.getRegularIntervelTransection());
//                    accountNode.setSuspicious(account.getSuspicious());
//                }
//
//                // Save the node (will update if exists, create if new)
//                accountNodeRepo.save(accountNode);
//
//                // Also save the original account
//
//

                accountRepo.save(account);
            }
        }

        // Move all transactions from Redis to transectionRepo
        Set<Object> transectionKeys = redisService.getSetMembers("transaction");
        if (transectionKeys != null) {
            for (Object transectionKey : transectionKeys) {
                Transection transaction = redisService.getObject(transectionKey.toString(), Transection.class);

//                Account senderacc= (Account) transaction.getSender();
//                Account receiveracc= (Account) transaction.getReceiver();
//
//
//                AccountNode senderNode = accountNodeRepo.findByAccountNumber(senderacc.getAccountNumber()).orElseThrow();
//                AccountNode receiverNode = accountNodeRepo.findByAccountNumber(receiveracc.getAccountNumber()).orElseThrow();
//
//
//                TransactionRelationship transactionEdge = new TransactionRelationship();
//
//                transactionEdge.setAmt(transaction.getAmt());
//                transactionEdge.setCurrency(transaction.getCurrency());
//                transactionEdge.setCreatedDate(transaction.getCreatedDate());
//                transactionEdge.setType(transaction.getType());
//                transactionEdge.setDescription(transaction.getDescription());
//                transactionEdge.setTargetAccount(receiverNode);
//                senderNode.getOutgoingTransactions().add(transactionEdge);
//
//
////                String url = "http://localhost:5000/predict"; // Flask running locally
////
////                RestTemplate restTemplate = new RestTemplate();
////                Map<String, Object> request = new HashMap<>();
////                request.put("features", transaction);
////
////                ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
////
////                System.out.println(Objects.requireNonNull(response.getBody()).get("prediction").toString());
//
//
//                accountNodeRepo.save(senderNode);
//
//
//
//
//
//                accountRepo.save(senderacc);
//                accountRepo.save(receiveracc);

                transectionRepo.save(transaction);
            }
        }
    }
    public boolean postDataToFlask(List<Account> accounts) {
        try {
            // Step 1: Convert to AccountDTOs
            List<AccountDTOforCSV> dtoList = new ArrayList<>();

            Set<Object> accountKeys = redisService.getSetMembers("accounts");
            if (accountKeys != null) {
                for (Object accountKey : accountKeys) {
                    Account account = redisService.getObject(accountKey.toString(), Account.class);
                    AccountDTOforCSV accountDTOforCSV=new AccountDTOforCSV(account.getAccountNumber() , account.getSuspicious());
                    dtoList.add(accountDTOforCSV);
                }
            }




                    // Step 3: Prepare HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Step 3: Prepare and send POST request
            HttpEntity<List<AccountDTOforCSV>> request = new HttpEntity<>(dtoList, headers);
            restTemplate.postForObject(uri + "/filtered-accounts", request, String.class);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}
