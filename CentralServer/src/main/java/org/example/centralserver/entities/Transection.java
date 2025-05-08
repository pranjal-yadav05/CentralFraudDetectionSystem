package org.example.centralserver.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "transections")
public class Transection implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id; // MongoDB will handle this automatically

    private Account sender;
    private Account receiver;
    private double amt;
    private String type;
    private String currency;
    private String description;
    private LocalDateTime createdDate;
    private Boolean suspicious=false;
    // Let Spring Data automatically handle the creation date

    public Transection(String id,Account sender, Account receiver, Double amount, String type, String currency,String description , Double balanceAfterTransection,LocalDateTime createdDate) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.amt = amount;
        this.type = type;
        this.currency = currency;
        this.description = description;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public Object getSender() {
        return sender;
    }

    public Object getReceiver() {
        return receiver;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public double getAmt() {
        return amt;
    }

    public Boolean getSuspicious() {
        return suspicious;
    }

    public void setSuspicious(Boolean suspicious) {
        this.suspicious = suspicious;
    }
    // Optionally, you can add a method to handle your own logic for creation date if needed
}
