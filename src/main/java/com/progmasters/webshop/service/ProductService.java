package com.progmasters.webshop.service;

import com.progmasters.webshop.domain.Product;
import com.progmasters.webshop.domain.dto.ProductCreationData;
import com.progmasters.webshop.domain.dto.ProductListItem;
import com.progmasters.webshop.repository.CategoryRepository;
import com.progmasters.webshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductService {

    private ProductRepository productRepository;

    private CategoryRepository categoryRepository;


    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public List<ProductListItem> getAll() {
        List<Product> products = productRepository.findAll();
        List<ProductListItem> productListItems = new ArrayList<>();

        for (Product product : products) {
            productListItems.add(new ProductListItem(product));
        }

        return productListItems;
    }


    public List<ProductListItem> getAllAvailable() {
        List<Product> products = productRepository.findAllByIsAvailableTrue();
        List<ProductListItem> productListItems = new ArrayList<>();

        for (Product product : products) {

            productListItems.add(new ProductListItem(product));
        }

        return productListItems;
    }

    public List<ProductListItem> getAllUnavailable() {
        List<Product> products = productRepository.findAllByIsAvailableFalse();
        List<ProductListItem> productListItems = new ArrayList<>();

        for (Product product : products) {

            productListItems.add(new ProductListItem(product));
        }

        return productListItems;
    }

    public ProductListItem getOne(Long id) {
        Product product = productRepository.findById(id);
        return new ProductListItem(product);
    }

    public Product getOneById(Long id) {
        return productRepository.findById(id);
    }

    public List<ProductListItem> listAllByCategoryId(Long categoryId) {
        List<ProductListItem> productList = new ArrayList<>();

        List<Product> products = productRepository.findAllByCategoryId(categoryId);


        for (Product product : products) {

            if (product.getAvailable()) {
                productList.add(new ProductListItem(product));
            }
        }

        return productList;
    }

    public void delete(Product product) {
        productRepository.delete(product);
    }

    public Product setToUnavailable(Product product) {
        product.setAvailable(false);

        return productRepository.save(product);
    }

    public Product setToAvailable(Product product) {
        product.setAvailable(true);

        return productRepository.save(product);
    }

    public ProductListItem updateProduct(ProductCreationData productCreationData, Long id) {
        Product product = productRepository.findById(id);

        ProductListItem productListItem;
        if (product != null) {
            updateValues(product, productCreationData);
            productListItem = new ProductListItem(product);
        } else {
            productListItem = null;
        }


        return productListItem;
    }

    private void updateValues(Product product, ProductCreationData productCreationData) {
        product.setName(productCreationData.getName());
        product.setPrice(productCreationData.getPrice());
        product.setDescription(productCreationData.getDescription());
        product.setPictureName(productCreationData.getPicture());

        if (!productCreationData.getCategoryName().equals("")) {
            product.setCategory(categoryRepository.findByName(productCreationData.getCategoryName()));
        } else {
            product.setCategory(null);
        }

        productRepository.save(product);

    }

    private File selectPicture(String pictureName) {
        Resource resource;
        if (!pictureName.equals("")) {
            resource = new ClassPathResource(pictureName);
        } else {
            resource = new ClassPathResource("noImage.png");
        }

        File file = null;
        try {
            file = resource.getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
