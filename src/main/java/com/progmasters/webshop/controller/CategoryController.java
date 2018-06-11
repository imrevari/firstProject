package com.progmasters.webshop.controller;

import com.progmasters.webshop.domain.Category;
import com.progmasters.webshop.domain.dto.CategoryCreationData;
import com.progmasters.webshop.domain.dto.ProductListItem;
import com.progmasters.webshop.service.CategoryService;
import com.progmasters.webshop.service.ProductService;
import com.progmasters.webshop.validator.CategoryCreationDataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private CategoryService categoryService;
    private ProductService productService;
    private CategoryCreationDataValidator categoryCreationDataValidator;

    @Autowired
    public CategoryController(CategoryService categoryService, ProductService productService, CategoryCreationDataValidator categoryCreationDataValidator) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.categoryCreationDataValidator = categoryCreationDataValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(categoryCreationDataValidator);
    }

    @GetMapping("/names") // - accessible to admin only
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<String>> getCategoryNames() {
        return new ResponseEntity<>(categoryService.findAllName(), HttpStatus.OK);
    }

    @GetMapping("/list/{productId}") // - accessible to unregistered users
    public ResponseEntity<List<ProductListItem>> getProductsByCategoryId(@PathVariable Long productId) {
        List<ProductListItem> productListItems = new ArrayList<>();

        try {
            productListItems = productService.listAllByCategoryId(productId);

        } catch (Exception e) {
            if (productListItems == null) {
                productListItems = new ArrayList<>();
        }
        }

        return new ResponseEntity<>(productListItems, HttpStatus.OK);
    }

    @PostMapping // - accessible to admin only
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createNewCategory(@Valid @RequestBody CategoryCreationData categoryCreationData) {
        Category category = new Category(categoryCreationData);

        categoryService.save(category);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
