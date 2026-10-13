package com.utp.model.entity;

import com.utp.util.enums.PayStatus;

import java.security.Timestamp;

public class PayEntity {
    private int idPay;
    private int idSale;
    private String paymentMethod;
    private double amount; //monto
    private PayStatus payStatus;
    private String transactionReference;// referencia de la operacion
    private Timestamp paymentDate;
    private Integer validatedBy;
    private Timestamp validationDate;
    private Timestamp creationDate;

    public PayEntity() {
    }

    public PayEntity(int idPay, int idSale, String paymentMethod, double amount, PayStatus payStatus,
                     String transactionReference, Timestamp paymentDate, Integer validatedBy, Timestamp validationDate,
                     Timestamp creationDate) {
        this.idPay = idPay;
        this.idSale = idSale;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.payStatus = payStatus;
        this.transactionReference = transactionReference;
        this.paymentDate = paymentDate;
        this.validatedBy = validatedBy;
        this.validationDate = validationDate;
        this.creationDate = creationDate;
    }

    public int getIdPay() {
        return idPay;
    }

    public void setIdPay(int idPay) {
        this.idPay = idPay;
    }

    public int getIdSale() {
        return idSale;
    }

    public void setIdSale(int idSale) {
        this.idSale = idSale;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PayStatus getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PayStatus payStatus) {
        this.payStatus = payStatus;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Integer getValidatedBy() {
        return validatedBy;
    }

    public void setValidatedBy(Integer validatedBy) {
        this.validatedBy = validatedBy;
    }

    public Timestamp getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(Timestamp validationDate) {
        this.validationDate = validationDate;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
}
