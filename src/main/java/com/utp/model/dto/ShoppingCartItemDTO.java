package com.utp.model.dto;

public class ShoppingCartItemDTO {
    private int idProduct;
    private Integer idProductVariant;
    private String name;
    private double unitPrice;
    private int quantity;

    public ShoppingCartItemDTO() {
    }

    public ShoppingCartItemDTO(int idProduct, Integer idProductVariant, String name, double unitPrice, int quantity) {
        this.idProduct = idProduct;
        this.idProductVariant = idProductVariant;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return this.unitPrice * this.quantity;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
