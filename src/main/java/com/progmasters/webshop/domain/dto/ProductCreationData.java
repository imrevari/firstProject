package com.progmasters.webshop.domain.dto;

public class ProductCreationData {

    private String name;

    private String description;

    private Double price;

    private String picture;

    private String categoryName;

    public ProductCreationData() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getPicture() {
        return picture;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
