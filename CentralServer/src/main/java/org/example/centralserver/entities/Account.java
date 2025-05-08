package org.example.centralserver.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@Getter
@Setter
@Document("accounts")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L; // Add a serialVersionUID for better version control during serialization

    @Id
    private String id;
    private String accountNumber;

    private double freq=0;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // Ensures consistent formatting
    private LocalDateTime lastTransaction=null;
    
    private int regularIntervelTransection=0;
    private boolean isSuspicious=false;
    private double suspiciousScore=0.0;
    private String bankId;


    public Account(){}
    public Account(String accountNumber , String bankId ,String id) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.bankId = bankId;
    }


    public String getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }


    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public double getFreq() {
        return freq;
    }

    public LocalDateTime getLastTransaction() {
        return lastTransaction;
    }

    public int getRegularIntervelTransection() {
        return regularIntervelTransection;
    }



    public void setId(String id) {
        this.id = id;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }



    public void setFreq(double freq) {
        this.freq = freq;
    }

    public void setLastTransaction(LocalDateTime lastTransaction) {
        this.lastTransaction = lastTransaction;
    }

    public void setRegularIntervelTransection(int regularIntervelTransection) {
        this.regularIntervelTransection = regularIntervelTransection;
    }

    public void setSuspicious(boolean suspicious) {
        isSuspicious = suspicious;
    }

    public boolean getSuspicious() {
        return isSuspicious;
    }

    public String getBankId() {
        return bankId;
    }

    public double getSuspiciousScore() {
        return suspiciousScore;
    }

    public void setSuspiciousScore(double suspiciousScore) {
        this.suspiciousScore = suspiciousScore;
    }

}
