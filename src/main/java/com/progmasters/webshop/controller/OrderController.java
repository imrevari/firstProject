package com.progmasters.webshop.controller;

import com.progmasters.webshop.domain.dto.OrderCreationData;
import com.progmasters.webshop.domain.dto.OrderListItem;
import com.progmasters.webshop.domain.dto.ProductInCart;
import com.progmasters.webshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> createNewOrder(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<ProductInCart> products = (List<ProductInCart>) session.getAttribute("cart");
        session.removeAttribute("cart");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = null;
        if (!authentication.getPrincipal().equals("anonymousUser")) {
            UserDetails user = (UserDetails) authentication.getPrincipal();
            email = user.getUsername();
        }

        orderService.createOrder(new OrderCreationData(email, products));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/myOrders")
    public ResponseEntity<List<OrderListItem>> findUserOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String email = user.getUsername();

        return new ResponseEntity<>(orderService.findAllByUser(email), HttpStatus.OK);
    }
}
