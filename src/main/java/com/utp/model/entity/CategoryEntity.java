package com.utp.model.entity;

import java.sql.Timestamp;

public class CategoryEntity {
    private int idCategory;
    private String nameCategory;
    private String descriptionCategory;
    private boolean categoryStatus;
    private Timestamp creationDate;

    public CategoryEntity() {
    }

    public CategoryEntity(int idCategory, String nameCategory, String descriptionCategory,
                          boolean categoryStatus, Timestamp creationDate) {
        this.idCategory = idCategory;
        this.nameCategory = nameCategory;
        this.descriptionCategory = descriptionCategory;
        this.categoryStatus = categoryStatus;
        this.creationDate = creationDate;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getDescriptionCategory() {
        return descriptionCategory;
    }

    public void setDescriptionCategory(String descriptionCategory) {
        this.descriptionCategory = descriptionCategory;
    }

    public boolean isCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(boolean categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
}
