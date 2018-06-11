package com.progmasters.webshop.service;

import com.progmasters.webshop.domain.Order;
import com.progmasters.webshop.domain.Product;
import com.progmasters.webshop.domain.WebshopUser;
import com.progmasters.webshop.domain.dto.OrderCreationData;
import com.progmasters.webshop.domain.dto.OrderListItem;
import com.progmasters.webshop.domain.dto.ProductInCart;
import com.progmasters.webshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private OrderRepository orderRepository;
    private UserService userService;
    private ProductService productService;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserService userService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.productService = productService;
    }

    public Order createOrder(OrderCreationData order) {
        Order newOrder = new Order();
        WebshopUser user = userService.findByEmail(order.getUserEmail());
        List<Product> products = new ArrayList<>();
        Double amount = 0d;

        for (ProductInCart p : order.getProducts()) {
            for (int i = 0; i < p.getAmount(); i++) {
                Product product = productService.getOneById(p.getProductId());
                products.add(product);
                amount += product.getPrice();
            }
        }

        newOrder.setUser(user);
        newOrder.setProducts(products);
        newOrder.setAmount(amount);
        newOrder.setOrderTime(LocalDateTime.now());

        return orderRepository.save(newOrder);
    }

    public List<OrderListItem> findAllByUser(String email) {
        List<Order> orders = orderRepository.findAllByUserOrderByOrderTimeDesc(userService.findByEmail(email));
        List<OrderListItem> result = new ArrayList<>();

        for (Order o : orders) {
            result.add(new OrderListItem(o));
        }

        return result;
    }
}
