package com.utp.model.dto;

public class ProductCatalogDTO {

    private final int idProduct;
    private final String name;
    private final String description;
    private final String category;
    private final double price;
    private final String imageUrl;
    private final int stockAvailable;
    private final boolean requiresCustomerData;

    public ProductCatalogDTO(
            int idProduct,
            String name,
            String description,
            String category,
            double price,
            String imageUrl,
            int stockAvailable,
            boolean requiresCustomerData) {

        this.idProduct = idProduct;
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stockAvailable = stockAvailable;
        this.requiresCustomerData = requiresCustomerData;
        this.description = description;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getStockAvailable() {
        return stockAvailable;
    }

    public boolean isRequiresCustomerData() {
        return requiresCustomerData;
    }

    public String getDescription() {
        return description;
    }
}
