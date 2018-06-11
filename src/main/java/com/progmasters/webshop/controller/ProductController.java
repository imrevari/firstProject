package com.progmasters.webshop.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.progmasters.webshop.domain.Category;
import com.progmasters.webshop.domain.Product;
import com.progmasters.webshop.domain.dto.ProductCreationData;
import com.progmasters.webshop.domain.dto.ProductInCart;
import com.progmasters.webshop.domain.dto.ProductListItem;
import com.progmasters.webshop.service.CategoryService;
import com.progmasters.webshop.service.CommentService;
import com.progmasters.webshop.service.ProductService;
import com.progmasters.webshop.validator.ProductCreationDataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static String UPLOADED_FOLDER = "C:\\Users\\madnorth\\IdeaProjects\\PROGmasters GitLab\\react-webshop\\src\\main\\resources\\";

    private ProductService productService;
    private CategoryService categoryService;
    private CommentService commentService;
    private ProductCreationDataValidator productCreationDataValidator;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, CommentService commentService, ProductCreationDataValidator productCreationDataValidator) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.commentService = commentService;
        this.productCreationDataValidator = productCreationDataValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(productCreationDataValidator);
    }


    @GetMapping // - accessible to unregistered users
    public ResponseEntity<List<ProductListItem>> getAvailableProducts() {
        return new ResponseEntity<>(productService.getAllAvailable(), HttpStatus.OK);
    }

    @GetMapping("/unavailableItems") // - accessible to admin only
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ProductListItem>> getUnavailableProducts() {
        return new ResponseEntity<>(productService.getAllUnavailable(), HttpStatus.OK);
    }

    @GetMapping("/{id}") // - accessible to unregistered users
    public ResponseEntity<ProductListItem> getProduct(@PathVariable Long id) {
        return new ResponseEntity<>(productService.getOne(id), HttpStatus.OK);
    }

    @PutMapping("/{id}") // - accessible to admin only
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductListItem> updateProduct(@Valid @RequestBody ProductCreationData productCreationData, @PathVariable Long id) {
        ProductListItem updatedProductListItem = productService.updateProduct(productCreationData, id);
        ResponseEntity<ProductListItem> result;

        if (updatedProductListItem == null) {
            result = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            result = new ResponseEntity<>(updatedProductListItem, HttpStatus.OK);
        }

        return result;
    }

    @PostMapping // - accessible to admin only
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createNewProduct(@Valid @RequestBody ProductCreationData productCreationData) {
        Product product = new Product(productCreationData);

        if (!productCreationData.getCategoryName().equals("")) {
            Category category = categoryService.findByName(productCreationData.getCategoryName());
            if (category != null) {
                product.setCategory(category);
            }
        } else {
            product.setCategory(null);
        }

        productService.save(product);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/putIntoCart/{id}") // - accessible to unregistered users
    public ResponseEntity<ProductInCart> putProductIntoCart(@PathVariable Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        ProductListItem product = productService.getOne(id);
        ProductInCart productInCart = new ProductInCart(product, 0);
        List<ProductInCart> productsInCart;

        if (session.getAttribute("cart") == null) {
            productsInCart = new ArrayList<>();
            productsInCart.add(new ProductInCart(product, 1));
        } else {
            productsInCart = (List<ProductInCart>) session.getAttribute("cart");
            if (productsInCart.indexOf(productInCart) == -1) {
                productsInCart.add(new ProductInCart(product, 1));
            } else {
                int index = productsInCart.indexOf(productInCart);
                productsInCart.set(index, new ProductInCart(product, productsInCart.get(index).getAmount() + 1));
            }
        }
        session.setAttribute("cart", productsInCart);

        return new ResponseEntity<>(productInCart, HttpStatus.OK);
    }

    @GetMapping("/myCart") // - accessible to unregistered users
    public ResponseEntity<List<ProductListItem>> myCart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<ProductListItem> products = (List<ProductListItem>) session.getAttribute("cart");
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PutMapping("/increaseQuantity/{id}") // - accessible to logged in users
    public ResponseEntity<List<ProductInCart>> increaseQuantity(@PathVariable Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<ProductInCart> products = (List<ProductInCart>) session.getAttribute("cart");
        List<ProductInCart> newCart = products.stream().map(item -> {
            if (item.getProductId().equals(id)) {
                item.setAmount(item.getAmount() + 1);
            }
            return item;
        }).collect(Collectors.toList());

        session.setAttribute("cart", newCart);
        return new ResponseEntity<>(newCart, HttpStatus.OK);
    }

    @PutMapping("/decreaseQuantity/{id}") // - accessible to logged in users
    public ResponseEntity<List<ProductInCart>> decreaseQuantity(@PathVariable Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<ProductInCart> products = (List<ProductInCart>) session.getAttribute("cart");
        List<ProductInCart> newCart = products.stream().map(item -> {
            if (item.getProductId().equals(id)) {
                item.setAmount(item.getAmount() - 1);
            }
            return item;
        }).filter(item -> item.getAmount() > 0).collect(Collectors.toList());

        session.setAttribute("cart", newCart);
        return new ResponseEntity<>(newCart, HttpStatus.OK);
    }

    @DeleteMapping("/emptyCart") // - accessible to logged in users
    public ResponseEntity<?> emptyCart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("cart") != null) {
            session.removeAttribute("cart");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}") // - accessible to admin only
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        Product product = productService.getOneById(id);
        if (product != null) {
            productService.setToUnavailable(product);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/setToAvailable/{id}") // - accessible to admin only
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> restoreProduct(@PathVariable Long id) {
        Product product = productService.getOneById(id);
        if (product != null) {
            productService.setToAvailable(product);

        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/uploadPic")
    public ResponseEntity<String> savePic(@RequestParam("file") MultipartFile file) {

        String url = "https://res.cloudinary.com/dw5zr3uob/image/upload/v1527081842/noImage.png";
        if (file.isEmpty()) {
            return new ResponseEntity<>(url, HttpStatus.OK);
        }

        try {
            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dw5zr3uob",
                    "api_key", "697575466792674",
                    "api_secret", "fbFQ12PUnMMyF2kl83sfQysad50"));

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            url = (String) uploadResult.get("url");
//            System.out.println(uploadResult.get("public_id"));
//            byte[] bytes = file.getBytes();
//            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
//            Files.write(path, bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(url, HttpStatus.OK);
    }

}

