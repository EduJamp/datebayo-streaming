package com.utp.model.entity;

public class ProductTypeEntity {
    private int idProductType;
    private String codeProductType;
    private String nameProductType;
    private boolean requiresCustomerData; //requiere datos del cliente true -> si, false -> no
    private boolean inventoryAllocationRequired; //requiere asignacion de inventario

    public ProductTypeEntity() {
    }

    public ProductTypeEntity(int idProductType, String codeProductType, String nameProductType,
                             boolean requiresCustomerData, boolean inventoryAllocationRequired) {
        this.idProductType = idProductType;
        this.codeProductType = codeProductType;
        this.nameProductType = nameProductType;
        this.requiresCustomerData = requiresCustomerData;
        this.inventoryAllocationRequired = inventoryAllocationRequired;
    }

    public int getIdProductType() {
        return idProductType;
    }

    public void setIdProductType(int idProductType) {
        this.idProductType = idProductType;
    }

    public String getCodeProductType() {
        return codeProductType;
    }

    public void setCodeProductType(String codeProductType) {
        this.codeProductType = codeProductType;
    }

    public String getNameProductType() {
        return nameProductType;
    }

    public void setNameProductType(String nameProductType) {
        this.nameProductType = nameProductType;
    }

    public boolean isRequiresCustomerData() {
        return requiresCustomerData;
    }

    public void setRequiresCustomerData(boolean requiresCustomerData) {
        this.requiresCustomerData = requiresCustomerData;
    }

    public boolean isInventoryAllocationRequired() {
        return inventoryAllocationRequired;
    }

    public void setInventoryAllocationRequired(boolean inventoryAllocationRequired) {
        this.inventoryAllocationRequired = inventoryAllocationRequired;
    }
}
