package com.progmasters.webshop.repository;

import com.progmasters.webshop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findById(Long id);

    List<Product> findAllByCategoryId(Long categoryId);

    List<Product> findAllByCategoryIdAndIsAvailableTrue(Long categoryId);

    List<Product> findAllByIsAvailableTrue();

    List<Product> findAllByIsAvailableFalse();
}
