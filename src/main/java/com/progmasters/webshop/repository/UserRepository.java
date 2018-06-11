package com.progmasters.webshop.repository;

import com.progmasters.webshop.domain.WebshopUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<WebshopUser, Long> {

    WebshopUser findByEmail(String email);

    WebshopUser findById(Long Id);


    List<WebshopUser> findAllByOrderById();
}
