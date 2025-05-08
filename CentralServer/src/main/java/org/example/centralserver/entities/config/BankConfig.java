package org.example.centralserver.entities.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.driver.TransactionConfig;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Document("BankConfig")
@Component
public class BankConfig {

    private String bankId;
    private String bankName;
    private String bankEmail;
    private TransectionConfig transectionConfig;
    private String transactionURI;
    private String databaseStructure;

    public String getDatabaseStructure() {
        return databaseStructure;
    }

    public String getBankId() {
        return bankId;
    }



    public TransectionConfig getTransactionConfig() {
        return transectionConfig;
    }


    public String getTransactionURI() {
        return transactionURI;
    }



    public void setBankId(String bankId) {
        this.bankId = bankId;
    }


    public void setTransectionConfig(TransectionConfig transectionConfig) {
        this.transectionConfig = transectionConfig;
    }

    public void setTransactionURI(String transactionURI) {
        this.transactionURI = transactionURI;
    }

    public void setDatabaseStructure(String databaseStructure) {
        this.databaseStructure = databaseStructure;
    }

    public String getBankEmail() {
        return bankEmail;
    }
    public void setBankEmail(String bankEmail) {
        this.bankEmail = bankEmail;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
