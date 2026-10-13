package com.utp.model.entity;

import com.utp.util.enums.DigitalAccountStatus;

import java.security.Timestamp;

public class DigitalAccountEntity {
    private int idDigitalAcount;
    private int idProduct;
    private String accountEmail;
    private String paswordHash;
    private int profileCapacity;
    private DigitalAccountStatus status;
    private Timestamp ecpirationDate;
    private String observations;
    private Integer createdBy;
    private Timestamp createdDate;

    public DigitalAccountEntity() {
    }

    public DigitalAccountEntity(int idDigitalAcount, int idProduct, String accountEmail, String paswordHash,
                                int profileCapacity, DigitalAccountStatus status, Timestamp ecpirationDate,
                                String observations, Integer createdBy, Timestamp createdDate) {
        this.idDigitalAcount = idDigitalAcount;
        this.idProduct = idProduct;
        this.accountEmail = accountEmail;
        this.paswordHash = paswordHash;
        this.profileCapacity = profileCapacity;
        this.status = status;
        this.ecpirationDate = ecpirationDate;
        this.observations = observations;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }

    public int getIdDigitalAcount() {
        return idDigitalAcount;
    }

    public void setIdDigitalAcount(int idDigitalAcount) {
        this.idDigitalAcount = idDigitalAcount;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getPaswordHash() {
        return paswordHash;
    }

    public void setPaswordHash(String paswordHash) {
        this.paswordHash = paswordHash;
    }

    public int getProfileCapacity() {
        return profileCapacity;
    }

    public void setProfileCapacity(int profileCapacity) {
        this.profileCapacity = profileCapacity;
    }

    public DigitalAccountStatus getStatus() {
        return status;
    }

    public void setStatus(DigitalAccountStatus status) {
        this.status = status;
    }

    public Timestamp getEcpirationDate() {
        return ecpirationDate;
    }

    public void setEcpirationDate(Timestamp ecpirationDate) {
        this.ecpirationDate = ecpirationDate;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}
