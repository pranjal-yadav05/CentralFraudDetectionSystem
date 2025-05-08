package org.example.centralserver.repo.mongo;

import org.example.centralserver.entities.config.BankConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BankConfigRepo extends MongoRepository<BankConfig, String> {
    Optional<BankConfig> findByBankId(String bankId);
}
