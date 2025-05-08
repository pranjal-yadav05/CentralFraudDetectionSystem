package org.example.centralserver.entities.DTO;

import java.time.LocalDateTime;

public class LinkDTO {
    private String source;
    private String target;
    private Double amount;
    private String type;
    private LocalDateTime date;

    public LinkDTO(){}
    public LinkDTO(String source, String target, Double amount, String type, LocalDateTime date) {
        this.source = source;
        this.target = target;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}