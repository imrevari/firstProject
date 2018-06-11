package com.progmasters.webshop.domain.dto;

import java.util.List;

public class OrderCreationData {

    private String userEmail;
    private List<ProductInCart> products;

    public OrderCreationData() {
    }

    public OrderCreationData(String userEmail, List<ProductInCart> products) {
        this.userEmail = userEmail;
        this.products = products;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public List<ProductInCart> getProducts() {
        return products;
    }
}
