package org.example.centralserver.services;

import org.example.centralserver.entities.DTO.GraphResponseDTO;
import org.example.centralserver.entities.DTO.LinkDTO;
import org.example.centralserver.entities.DTO.NodeDTO;
import org.example.centralserver.entities.neo4j.AccountNode;
import org.example.centralserver.repo.neo4j.AccountNodeRepository;
import org.neo4j.driver.types.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.neo4j.driver.types.Relationship;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class GraphService {

    private final AccountNodeRepository accountNodeRepository;
    private final Neo4jClient neo4jClient;

    @Autowired
    public GraphService(AccountNodeRepository accountNodeRepository,Neo4jClient neo4jClient) {
        this.accountNodeRepository = accountNodeRepository;
        this.neo4jClient=neo4jClient;
    }

    public List<AccountNode> getAllAccounts() {
        return accountNodeRepository.findAllAccounts();
    }

    public GraphResponseDTO getNetworkGraph(String accountNumber, Integer level) {
        List<Map<String, Object>> results = findNetworkByAccountNumberAndLevel(accountNumber, level);

        Map<String, NodeDTO> nodesMap = new HashMap<>();
        Map<Long, String> nodeIdToAccountNumber = new HashMap<>();
        List<LinkDTO> links = new ArrayList<>();

        // Track relationships by their actual Neo4j ID instead of node pairs
        Set<Long> processedRelationshipIds = new HashSet<>();

        for (Map<String, Object> result : results) {
            Path path = (Path) result.get("path");

            // Process nodes
            for (org.neo4j.driver.types.Node node : path.nodes()) {
                String nodeAccountNumber = node.get("accountNumber").asString();
                nodeIdToAccountNumber.put(node.id(), nodeAccountNumber);

                if (!nodesMap.containsKey(nodeAccountNumber)) {
                    NodeDTO nodeDTO = new NodeDTO();
                    nodeDTO.setId(nodeAccountNumber);
                    nodeDTO.setAccountNumber(nodeAccountNumber);

                    // Handle optional properties
                    if (node.containsKey("type") && !node.get("type").isNull()) {
                        nodeDTO.setType(node.get("type").asString());
                    }

                    if (node.containsKey("balance") && !node.get("balance").isNull()) {
                        nodeDTO.setBalance(node.get("balance").asDouble());
                    }

                    if (node.containsKey("isSuspicious") && !node.get("isSuspicious").isNull()) {
                        nodeDTO.setSuspicious(node.get("isSuspicious").asBoolean());
                    }

                    nodesMap.put(nodeAccountNumber, nodeDTO);
                }
            }

            // Process relationships using the actual relationship ID to ensure uniqueness
            for (Relationship relationship : path.relationships()) {
                // Only process each unique relationship once by its Neo4j ID
                if (!processedRelationshipIds.contains(relationship.id())) {
                    processedRelationshipIds.add(relationship.id());

                    String sourceAccountNumber = nodeIdToAccountNumber.get(relationship.startNodeId());
                    String targetAccountNumber = nodeIdToAccountNumber.get(relationship.endNodeId());

                    LinkDTO linkDTO = new LinkDTO();
                    linkDTO.setSource(sourceAccountNumber);
                    linkDTO.setTarget(targetAccountNumber);

                    // Handle optional properties
                    if (relationship.containsKey("amt") && !relationship.get("amt").isNull()) {
                        linkDTO.setAmount(relationship.get("amt").asDouble());
                    }

                    if (relationship.containsKey("type") && !relationship.get("type").isNull()) {
                        linkDTO.setType(relationship.get("type").asString());
                    }

                    if (relationship.containsKey("createdDate") && !relationship.get("createdDate").isNull()) {
                        String dateStr = relationship.get("createdDate").asString(); // Read as String
                        LocalDateTime createdDate = LocalDateTime.parse(dateStr);    // Parse to LocalDateTime
                        linkDTO.setDate(createdDate);
                    }

                    links.add(linkDTO);
                }
            }
        }

        GraphResponseDTO response = new GraphResponseDTO();
        response.setNodes(new ArrayList<>(nodesMap.values()));
        response.setLinks(links);

        return response;
    }

    public List<Map<String, Object>> findNetworkByAccountNumberAndLevel(String accountNumber, int level) {
        String query = "MATCH path = (a:Account {accountNumber: $accountNumber})-[r:TRANSACTION*1.." + level + "]-(related) RETURN path";

        return (List<Map<String, Object>>) neo4jClient.query(query)
                .bindAll(Map.of("accountNumber", accountNumber))
                .fetch().all();
    }

}