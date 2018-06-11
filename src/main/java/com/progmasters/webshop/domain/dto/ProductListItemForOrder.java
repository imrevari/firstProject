package com.progmasters.webshop.domain.dto;

import com.progmasters.webshop.domain.Product;

public class ProductListItemForOrder {

    private Long productId;
    private String name;
    private String imgURL;
    private Integer quantity;

    public ProductListItemForOrder() {
    }

    public ProductListItemForOrder(Product product, Integer quantity) {
        this.productId = product.getId();
        this.name = product.getName();
        this.imgURL = product.getPictureName();
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductListItemForOrder that = (ProductListItemForOrder) o;

        return productId != null ? productId.equals(that.productId) : that.productId == null;
    }

    @Override
    public int hashCode() {
        return productId != null ? productId.hashCode() : 0;
    }
}
