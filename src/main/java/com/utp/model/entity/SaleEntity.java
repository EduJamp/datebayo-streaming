package com.utp.model.entity;

import com.utp.util.enums.SalesStatus;

import java.time.LocalDateTime;

public class SaleEntity {
    private int idSale; //venta
    private String salesNumber;
    private int idClient;
    private Integer idEmployee;//empleado responsable
    private SalesStatus saleStatus;
    private double subtotal;
    private double totalDiscount;
    private double total;
    private LocalDateTime creationDate;
    private LocalDateTime confirmationDate;
    private String observation;

    public SaleEntity() {
    }

    public SaleEntity(int idSale, String salesNumber, int idClient, Integer idEmployee, SalesStatus saleStatus,
                      double subtotal, double totalDiscount, double total, LocalDateTime creationDate, LocalDateTime confirmationDate,
                      String observation) {
        this.idSale = idSale;
        this.salesNumber = salesNumber;
        this.idClient = idClient;
        this.idEmployee = idEmployee;
        this.saleStatus = saleStatus;
        this.subtotal = subtotal;
        this.totalDiscount = totalDiscount;
        this.total = total;
        this.creationDate = creationDate;
        this.confirmationDate = confirmationDate;
        this.observation = observation;
    }

    public int getIdSale() {
        return idSale;
    }

    public void setIdSale(int idSale) {
        this.idSale = idSale;
    }

    public String getSalesNumber() {
        return salesNumber;
    }

    public void setSalesNumber(String salesNumber) {
        this.salesNumber = salesNumber;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public Integer getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Integer idEmployee) {
        this.idEmployee = idEmployee;
    }

    public SalesStatus getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(SalesStatus saleStatus) {
        this.saleStatus = saleStatus;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(LocalDateTime confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
