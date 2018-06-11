package com.progmasters.webshop.domain.dto;

import com.progmasters.webshop.domain.Product;

public class ProductListItem {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String picture;
    private String categoryName;
    private Long categoryId;

    public ProductListItem(Long id, String name, String description, Double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }


    public ProductListItem() {
    }

    public ProductListItem(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        if (product.getCategory() != null) {
            this.categoryName = product.getCategory().getName();
            this.categoryId = product.getCategory().getId();
        } else {
            categoryName = "N/A";
        }
        this.picture = product.getPictureName();
    }

    public Long getId() {
        return id;
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

    public Long getCategoryId() {
        return categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductListItem that = (ProductListItem) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
