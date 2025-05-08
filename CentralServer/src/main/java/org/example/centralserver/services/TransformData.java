package org.example.centralserver.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.centralserver.entities.Account;
import org.example.centralserver.entities.Transection;
import org.example.centralserver.entities.config.BankConfig;
import org.example.centralserver.entities.config.TransectionConfig;
import org.example.centralserver.repo.mongo.BankConfigRepo;
import org.example.centralserver.repo.mongo.TransectionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TransformData {

    @Autowired
    BankConfig bankConfig;

    @Autowired
    BankConfigRepo bankConfigRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    TransectionRepo transectionRepo;

    private boolean isEmptyConfig(String configValue) {
        return configValue == null || configValue.isEmpty() || configValue.equalsIgnoreCase("none");
    }

        public List<Transection> convertAndProcessData(BankConfig bankConfig) {
            JsonNode rawTransaction = fetchRawData(bankConfig.getTransactionURI()).get("transactions");
            return processTransactions(bankConfig, rawTransaction);
        }

        private JsonNode fetchRawData(String uri) {
            try {
                String response = restTemplate.getForObject(uri, String.class);
                return objectMapper.readTree(response);
            } catch (Exception e) {
                System.out.println(e);
                throw new RuntimeException("Error fetching data", e);
            }
        }

    public List<Transection> processTransactions(BankConfig config, JsonNode rawData) {
        List<Transection> transformedTransactions = new ArrayList<>();

        if (rawData.isArray()) {
            // Handle array of transactions
            for (JsonNode transactionNode : rawData) {
                transformedTransactions.add(transformTransaction(transactionNode, config));
            }
        } else {
            // Handle single transaction
            transformedTransactions.add(transformTransaction(rawData, config));
        }

        return transformedTransactions;
    }

    private Transection transformTransaction(JsonNode rawData, BankConfig config) {
        Transection transection = new Transection();
        TransectionConfig txConfig = config.getTransactionConfig();

        // Map basic transaction fields
        mapFields(transection, rawData, Map.of(
                "id",txConfig.getId(),
                "amt", txConfig.getAmt(),
                "currency", txConfig.getCurrency(),
                "description", txConfig.getDescription(),
                "createdDate", txConfig.getCreatedDate()
        ));

        // Set sender and receiver as simple strings (account numbers or identifiers)
        String senderPath = txConfig.getSender();
        String receiverPath = txConfig.getReceiver();

        if (!isEmptyConfig(senderPath)) {
            String sender = getValueByPath(rawData, senderPath);
            Account senderAccount=new Account(sender,bankConfig.getBankId() , sender);
            senderAccount.setBankId(bankConfig.getBankId());
            transection.setSender(senderAccount);
        }

        if (!isEmptyConfig(receiverPath)) {
            String receiver = getValueByPath(rawData, receiverPath);
            Account receiverAccount=new Account(receiver,bankConfig.getBankId() , receiver);
            receiverAccount.setBankId(bankConfig.getBankId());
            transection.setReceiver(receiverAccount);
        }

        // Set suspicious flag default to false
        transection.setSuspicious(false);

        return transection;
    }

    /**
     * Generic method to map fields based on configuration
     */
    private void mapFields(Object target, JsonNode source, Map<String, String> fieldMappings) {
        try {
            for (Map.Entry<String, String> mapping : fieldMappings.entrySet()) {
                String targetField = mapping.getKey();
                String sourcePath = mapping.getValue();

                // Check if the config mapping is empty/null/none
                if (isEmptyConfig(sourcePath)) {
                    Field field = target.getClass().getDeclaredField(targetField);
                    field.setAccessible(true);
                    field.set(target, null);
                    continue;
                }

                JsonNode valueNode = getNodeByPath(source, sourcePath);
                if (valueNode != null && !valueNode.isNull()) {
                    Field field = target.getClass().getDeclaredField(targetField);
                    field.setAccessible(true);

                    Object value = convertNodeToFieldType(valueNode, field.getType());
                    field.set(target, value);
                } else {
                    // If path exists in config but value not found in source, set null
                    Field field = target.getClass().getDeclaredField(targetField);
                    field.setAccessible(true);
                    field.set(target, null);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("Error mapping fields", e);
        }
    }

    /**
     * Converts JsonNode value to appropriate Java type
     */
    private Object convertNodeToFieldType(JsonNode node, Class<?> targetType) {
        if (targetType == String.class) {
            return node.asText();
        } else if (targetType == Double.class || targetType == double.class) {
            return node.asDouble();
        } else if (targetType == Integer.class || targetType == int.class) {
            return node.asInt();
        } else if (targetType == LocalDateTime.class) {
            return LocalDateTime.parse(node.asText());
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            return node.asBoolean();
        }
        // Add more type conversions as needed
        return node.asText();
    }

    private String getValueByPath(JsonNode node, String path) {
        JsonNode result = getNodeByPath(node, path);
        return result != null ? result.asText() : null;
    }

    private JsonNode getNodeByPath(JsonNode node, String path) {
        if (path == null || path.isEmpty()) return null;

        String[] parts = path.split("\\.");
        JsonNode current = node;

        for (String part : parts) {
            if (current == null) return null;
            current = current.get(part);
        }

        return current;
    }
}