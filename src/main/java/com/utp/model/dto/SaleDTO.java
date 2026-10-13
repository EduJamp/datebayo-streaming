package com.utp.model.dto;

import com.utp.util.enums.PayStatus;
import com.utp.util.enums.SalesStatus;

public class SaleDTO {
    private int idSale;
    private String saleNumber;
    private String clientName;
    private String clienteEmail;
    private double total;
    private SalesStatus salesStatus;
    private String paymentMethod;
    private PayStatus payStatus;
    private String creationDate;
    private String voucherFileName;

    public SaleDTO(int idSale, String saleNumber, String clientName, String clienteEmail,
                   double total, SalesStatus salesStatus, String paymentMethod,
                   PayStatus payStatus, String creationDate, String voucherFileName) {
        this.idSale = idSale;
        this.saleNumber = saleNumber;
        this.clientName = clientName;
        this.clienteEmail = clienteEmail;
        this.total = total;
        this.salesStatus = salesStatus;
        this.paymentMethod = paymentMethod;
        this.payStatus = payStatus;
        this.creationDate = creationDate;
        this.voucherFileName = voucherFileName;
    }

    public int getIdSale() {
        return idSale;
    }

    public String getSaleNumber() {
        return saleNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClienteEmail() {
        return clienteEmail;
    }

    public double getTotal() {
        return total;
    }

    public SalesStatus getSalesStatus() {
        return salesStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public PayStatus getPayStatus() {
        return payStatus;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getVoucherFileName() {
        return voucherFileName;
    }
}
