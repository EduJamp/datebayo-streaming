package com.utp.model.entity;

import com.utp.util.enums.ProductStatus;

import java.sql.Timestamp;

public class ProductEntity {
    private int idProduct;
    private int idCategory;
    private int idProductType;
    private String name;
    private String description;
    private String urlImage;
    private double price;
    private ProductStatus productStatus;
    private boolean allowsMiltiplePurchases; //permite compra multiple
    private int minimumQuantity;
    private Integer macximumQuantity;
    private Integer createdBy;
    private Timestamp createdDate; //creado por
    private Timestamp updatedDate; //fecha de actualizacion

    public ProductEntity() {
    }

    public ProductEntity(int idProduct, int idCategory, int idProductType, String name, String description,
                         String urlImage, double price, ProductStatus productStatus, boolean allowsMiltiplePurchases,
                         int minimumQuantity, Integer macximumQuantity, Integer createdBy, Timestamp createdDate,
                         Timestamp updatedDate) {
        this.idProduct = idProduct;
        this.idCategory = idCategory;
        this.idProductType = idProductType;
        this.name = name;
        this.description = description;
        this.urlImage = urlImage;
        this.price = price;
        this.productStatus = productStatus;
        this.allowsMiltiplePurchases = allowsMiltiplePurchases;
        this.minimumQuantity = minimumQuantity;
        this.macximumQuantity = macximumQuantity;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public int getIdProductType() {
        return idProductType;
    }

    public void setIdProductType(int idProductType) {
        this.idProductType = idProductType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public boolean isAllowsMiltiplePurchases() {
        return allowsMiltiplePurchases;
    }

    public void setAllowsMiltiplePurchases(boolean allowsMiltiplePurchases) {
        this.allowsMiltiplePurchases = allowsMiltiplePurchases;
    }

    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public Integer getMacximumQuantity() {
        return macximumQuantity;
    }

    public void setMacximumQuantity(Integer macximumQuantity) {
        this.macximumQuantity = macximumQuantity;
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

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
