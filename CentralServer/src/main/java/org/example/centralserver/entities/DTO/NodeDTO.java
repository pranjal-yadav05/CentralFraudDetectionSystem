package org.example.centralserver.entities.DTO;
// src/main/java/com/banking/fraud/dto/NodeDTO.java


public class NodeDTO {
    private String id;
    private String accountNumber;
    private String type;
    private Double balance;
    private Boolean isSuspicious;

    public NodeDTO() {}
    public NodeDTO(String id, String accountNumber, String type, Double balance, Boolean isSuspicious) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.type = type;
        this.balance = balance;
        this.isSuspicious = isSuspicious;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Boolean getSuspicious() {
        return isSuspicious;
    }

    public void setSuspicious(Boolean suspicious) {
        isSuspicious = suspicious;
    }
}