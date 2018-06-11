package com.progmasters.webshop.domain.dto;

import com.progmasters.webshop.domain.Order;
import com.progmasters.webshop.domain.Product;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderListItem {

    private List<ProductListItemForOrder> orderedProducts = new ArrayList<>();
    private String orderTime;
    private Double amount;

    public OrderListItem(Order order) {
        Map<Product, Integer> ordersMap = new HashMap<>();
        for (Product p : order.getProducts()) {
            if (ordersMap.containsKey(p)) {
                Integer tempQuantity = ordersMap.get(p);
                ordersMap.replace(p, ++tempQuantity);
            } else {
                ordersMap.put(p, 1);
            }
        }

        for (Map.Entry<Product, Integer> e : ordersMap.entrySet()) {
            this.orderedProducts.add(new ProductListItemForOrder(e.getKey(), e.getValue()));
        }

        this.orderTime = order.getOrderTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.amount = order.getAmount();
    }

    public List<ProductListItemForOrder> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(List<ProductListItemForOrder> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
