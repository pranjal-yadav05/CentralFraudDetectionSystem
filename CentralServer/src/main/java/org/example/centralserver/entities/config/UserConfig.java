package org.example.centralserver.entities.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("UserConfig")
public class UserConfig {

    private String id;
    private String name;
    private String govIdNum;
    private String email;
    private String mobileNumber;
    private String idType;
    private String address;
    private String accounts;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGovIdNum() {
        return govIdNum;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getIdType() {
        return idType;
    }

    public String getAddress() {
        return address;
    }

    public String getAccounts() {
        return accounts;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGovIdNum(String govIdNum) {
        this.govIdNum = govIdNum;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAccounts(String accounts) {
        this.accounts = accounts;
    }
}
