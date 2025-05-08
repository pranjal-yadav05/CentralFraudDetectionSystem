package org.example.centralserver.repo.mongo;

import org.example.centralserver.entities.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccountRepo extends MongoRepository<Account, String> {

    long countByIsSuspiciousTrue();

    Optional<Account> findByAccountNumber(String accountNumber);
}
