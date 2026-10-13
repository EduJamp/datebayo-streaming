package com.utp.model.entity;

public class ProductVariantEntity {
    private int idProductVariant;
    private int idProduct;
    private String name;
    private Integer virtualQuantity;
    private double price;
    private boolean state;

    public ProductVariantEntity() {
    }

    public ProductVariantEntity(int idProductVariant, int idProduct, String name, Integer virtualQuantity,
                                double price, boolean state) {
        this.idProductVariant = idProductVariant;
        this.idProduct = idProduct;
        this.name = name;
        this.virtualQuantity = virtualQuantity;
        this.price = price;
        this.state = state;
    }

    public int getIdProductVariant() {
        return idProductVariant;
    }

    public void setIdProductVariant(int idProductVariant) {
        this.idProductVariant = idProductVariant;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVirtualQuantity() {
        return virtualQuantity;
    }

    public void setVirtualQuantity(Integer virtualQuantity) {
        this.virtualQuantity = virtualQuantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
