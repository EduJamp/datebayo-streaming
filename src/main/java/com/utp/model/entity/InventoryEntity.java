package com.utp.model.entity;

import java.security.Timestamp;

public class InventoryEntity {
    private int idInventory;
    private int idProduct;
    private Integer idProductVariant;
    private int quantityAvailable;
    private int quantityReserved;
    private int minimumQuantity;
    private Timestamp udateDate;

    public InventoryEntity() {
    }

    public InventoryEntity(int idInventory, int idProduct, Integer idProductVariant, int quantityAvailable,
                           int quantityReserved, int minimumQuantity, Timestamp udateDate) {
        this.idInventory = idInventory;
        this.idProduct = idProduct;
        this.idProductVariant = idProductVariant;
        this.quantityAvailable = quantityAvailable;
        this.quantityReserved = quantityReserved;
        this.minimumQuantity = minimumQuantity;
        this.udateDate = udateDate;
    }

    public int getIdInventory() {
        return idInventory;
    }

    public void setIdInventory(int idInventory) {
        this.idInventory = idInventory;
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

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public int getQuantityReserved() {
        return quantityReserved;
    }

    public void setQuantityReserved(int quantityReserved) {
        this.quantityReserved = quantityReserved;
    }

    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public Timestamp getUdateDate() {
        return udateDate;
    }

    public void setUdateDate(Timestamp udateDate) {
        this.udateDate = udateDate;
    }
}
