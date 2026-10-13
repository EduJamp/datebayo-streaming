package com.utp.model.entity;

public class SalesDatailsEntity {
    private int idSalesDetails;
    private int idSale;
    private int idProduct;
    private Integer idProductVariant;
    private String nameProductSnapsoht;
    private double unitPrice;
    private int quantity;
    private double subtotal;
    private double discount;
    private double lineTotal;

    public SalesDatailsEntity() {
    }

    public SalesDatailsEntity(int idSalesDetails, int idSale, int idProduct, Integer idProductVariant,
                              String nameProductSnapsoht, double unitPrice, int quantity, double subtotal, double discount,
                              double lineTotal) {
        this.idSalesDetails = idSalesDetails;
        this.idSale = idSale;
        this.idProduct = idProduct;
        this.idProductVariant = idProductVariant;
        this.nameProductSnapsoht = nameProductSnapsoht;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subtotal = subtotal;
        this.discount = discount;
        this.lineTotal = lineTotal;
    }

    public int getIdSalesDetails() {
        return idSalesDetails;
    }

    public void setIdSalesDetails(int idSalesDetails) {
        this.idSalesDetails = idSalesDetails;
    }

    public int getIdSale() {
        return idSale;
    }

    public void setIdSale(int idSale) {
        this.idSale = idSale;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public Integer getIdProductVariant() {
        return idProductVariant;
    }

    public void setIdProductVariant(Integer idProductVariant) {
        this.idProductVariant = idProductVariant;
    }

    public String getNameProductSnapsoht() {
        return nameProductSnapsoht;
    }

    public void setNameProductSnapsoht(String nameProductSnapsoht) {
        this.nameProductSnapsoht = nameProductSnapsoht;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }
}
