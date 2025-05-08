package org.example.centralserver.helper;

import lombok.extern.slf4j.Slf4j;
import org.example.centralserver.entities.Account;
import org.example.centralserver.entities.Transection;
import org.example.centralserver.services.AccountService;
import org.example.centralserver.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class AccountLoader {
    private static final Map<String, Lock> accountLocks = new ConcurrentHashMap<>();
    private static final long LOCK_TIMEOUT_MS = 5000; // Reduced to 5 seconds
    private final int MAX_RETRY_ATTEMPTS = 3;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AccountService accountService;
    

    public Account loadAccountIntoRedis(Account acc, Transection transection, String bank , Boolean isSender) {
        String lockKey = bank + "_" + acc.getId();
        int attempts = 0;


        //storing log of skipped transaction

        String accId=acc.getId();
        while (attempts < MAX_RETRY_ATTEMPTS) {
            Lock accountLock = accountLocks.computeIfAbsent(lockKey, k -> new ReentrantLock(true)); // Fair locking

            try {
                if (accountLock.tryLock(LOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                    try {
                        return processAccount(lockKey, acc, transection, bank , isSender);
                    } finally {
                        accountLock.unlock();
                        // Clean up if no other threads are waiting
                        if (((ReentrantLock) accountLock).getQueueLength() == 0) {
                            accountLocks.remove(lockKey);
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted while processing account: " + accId);
            }

            attempts++;
            if (attempts < MAX_RETRY_ATTEMPTS) {
                // Add small random delay before retry
                try {
                    Thread.sleep(100 + (long)(Math.random() * 400));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        throw new RuntimeException("Could not acquire lock for account: " + accId + " after " + MAX_RETRY_ATTEMPTS + " attempts");
    }

    private Account processAccount(String redisKey, Account account, Transection transection, String bank, Boolean isSender) {
        // First try to find account in Redis cache
        Account existingAccount = redisService.getObject(redisKey, Account.class);

        if (existingAccount != null) {
            // Account found in Redis, use it
            System.out.println("Account found in Redis: " + redisKey);
            updateAccountDetails(existingAccount, transection, redisKey, isSender);
            return existingAccount;
        }

        // Not found in Redis, try to find in accountRepo
        if (account == null) {
            // Try to extract accountId from redisKey if needed
            String accountId = extractAccountIdFromRedisKey(redisKey);
            existingAccount = accountService.getaccount(accountId);

            if (existingAccount != null) {
                // Account found in repository, cache it in Redis then update
                System.out.println("Account found in repository: " + accountId);
                redisService.saveObject(redisKey, existingAccount);
                updateAccountDetails(existingAccount, transection, redisKey, isSender);
                return existingAccount;
            } else {
                // Not found anywhere, create new account
                System.out.println("Creating new account for: " + redisKey);
                Account newAccount = createNewAccount(bank, transection, redisKey);
                redisService.saveObject(redisKey, newAccount);
                updateAccountDetails(newAccount, transection, redisKey, isSender);
                return newAccount;
            }
        } else {
            // Account was provided as parameter, use it
            redisService.saveObject(redisKey, account);
            updateAccountDetails(account, transection, redisKey, isSender);
            return account;
        }
    }

    // Helper method to extract account ID from Redis key if needed
    private String extractAccountIdFromRedisKey(String redisKey) {
        // Implement based on your key format
        // For example, if your key is "account:123", return "123"
        if (redisKey.contains(":")) {
            return redisKey.split(":")[1];
        }
        return redisKey;
    }

    // Helper method to create a new account
    private Account createNewAccount(String bank, Transection transection, String redisKey) {
        Account newAccount = new Account();
        // Set initial properties
        newAccount.setBankId(bank);
        newAccount.setFreq(0);
        newAccount.setSuspicious(false);
        newAccount.setLastTransaction(transection.getCreatedDate());

        // Extract and set account ID if needed
        String accountId = extractAccountIdFromRedisKey(redisKey);
        newAccount.setId(accountId);

        // Additional initialization as needed

        // Optional: Save to persistent storage
        accountService.addaccount(newAccount);

        return newAccount;
    }

    private void updateAccountDetails(Account account, Transection transection, String redisKey, boolean isSender) {
        Long newFreq = redisService.increment(redisKey + ":freq");
        account.setFreq(newFreq.intValue());

        account.setLastTransaction(transection.getCreatedDate());

        if (transection.getAmt() > 200000 && Objects.equals(transection.getCurrency(), "INR")) {
            transection.setSuspicious(true);
            account.setSuspicious(true);
        }
        else if (transection.getAmt() > 25000 && Objects.equals(transection.getCurrency(), "USD")) {
            transection.setSuspicious(true);
            account.setSuspicious(true);
        }

        if(account.getFreq() > 30) {
            account.setSuspicious(true);
        }

        redisService.saveObject(redisKey, account);
        redisService.addToSet("accounts", redisKey);

        System.out.println(transection.getId());

        redisService.saveObject(transection.getId(), transection);
        redisService.addToSet("transaction", transection.getId());


    }
}