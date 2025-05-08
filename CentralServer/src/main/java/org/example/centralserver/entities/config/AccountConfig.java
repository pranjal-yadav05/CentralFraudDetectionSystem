package org.example.centralserver.entities.config;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("AccountConfig")
public class AccountConfig {


    private String id;
    private String accountNumber;
    private String type;
    private String businessType;
    private String balance;
    private String user;
    private String nominees;

    public String getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getType() {
        return type;
    }

    public String getBusinessType() {
        return businessType;
    }

    public String getBalance() {
        return balance;
    }

    public String getUser() {
        return user;
    }

    public String getNominees() {
        return nominees;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setNominees(String nominees) {
        this.nominees = nominees;
    }
}
