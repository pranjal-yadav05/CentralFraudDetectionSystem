package org.example.centralserver.repo.mongo;

import org.example.centralserver.entities.Transection;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransectionRepo extends MongoRepository<Transection, String> {

    long countBySuspiciousTrue();


}
