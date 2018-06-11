package com.progmasters.webshop.repository;

import com.progmasters.webshop.domain.Order;
import com.progmasters.webshop.domain.WebshopUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUser(WebshopUser user);

    List<Order> findAllByUserOrderByOrderTimeDesc(WebshopUser user);

}
