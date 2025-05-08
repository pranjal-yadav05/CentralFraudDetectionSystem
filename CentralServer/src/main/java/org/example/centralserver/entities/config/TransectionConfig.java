package org.example.centralserver.entities.config;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("TransectionConfig")

public class TransectionConfig {

    private String id;
    private String sender;
    private String receiver;
    private String amt;
    private String currency;
    private String description;
    private String createdDate;

    public TransectionConfig() {}
    public TransectionConfig(String id, String sender, String receiver, String amt, String currency, String description , String createdDate) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.amt = amt;
        this.currency = currency;
        this.description = description;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getAmt() {
        return amt;
    }



    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }


    public String getCreatedDate() {
        return createdDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }


    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String toString() {
        return this.id+this.sender+this.receiver+this.amt+this.currency+this.description+this.createdDate;
    }
}
