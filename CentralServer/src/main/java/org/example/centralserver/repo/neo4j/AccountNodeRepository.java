package org.example.centralserver.repo.neo4j;

import org.example.centralserver.entities.neo4j.AccountNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface AccountNodeRepository extends Neo4jRepository<AccountNode, Long> {

    @Query("MATCH (a:Account) RETURN a")
    List<AccountNode> findAllAccounts();



    @Query("MATCH path = (a:Account {accountNumber: $accountNumber})-[r:TRANSACTS_WITH*1..$level]-(related) RETURN path")
    List<Map<String, Object>> findNetworkByAccountNumberAndLevel(
            @Param("accountNumber") String accountNumber,
            @Param("level") Integer level);

    Optional<AccountNode> findByAccountNumber(String accountNumber);
}

