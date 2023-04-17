package com.example.webshop.controllers;

import com.example.webshop.models.dto.Comment;
import com.example.webshop.models.dto.Product;
import com.example.webshop.models.entities.ProductEntity;
import com.example.webshop.models.requests.SearchProduct;
import com.example.webshop.models.requests.CommentRequest;
import com.example.webshop.models.requests.ProductRequest;
import com.example.webshop.models.requests.PurchaseRequest;
import com.example.webshop.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public Page<Product> findAll(Pageable page) {
        return service.findAll(page);
    }

    @GetMapping("/active")
    public Page<Product> findAllActive(Pageable page, @RequestParam(required = false) String productName) {
        return service.findAllActive(page, productName);
    }

    @GetMapping("/product/{id}")
    public Product findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @GetMapping("/{categoryName}")
    public Page<Product> findAllByCategory(Pageable page, @PathVariable String categoryName) {
        return service.findAllByCategory(page, categoryName);
    }

    // TODO: Search by general product attributes
    @PostMapping("/search")
    public Page<Product> searchProducts(Pageable page, @RequestBody SearchProduct searchProduct) {
        return service.search(page, searchProduct);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product insert(@RequestBody @Valid ProductRequest productRequest, Authentication authentication) {
        return service.insert(productRequest, authentication);
    }

    @DeleteMapping("/{id}")
    public Product delete(@PathVariable Integer id, Authentication authentication) {
        return service.delete(id, authentication);
    }

    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment comment(@RequestBody @Valid CommentRequest commentRequest, @PathVariable Integer id, Authentication authentication) {
        System.err.println("Comment of product.");
        commentRequest.setProductId(id);
        if (authentication == null) {
            System.err.println("Authentication is null.");
        }
        return service.comment(commentRequest, authentication);
    }

    @PostMapping("/{id}/purchase")
    public Product purchase(@PathVariable Integer id, @RequestBody @Valid PurchaseRequest purchaseRequest, Authentication authentication) {
        return service.purchase(id, purchaseRequest, authentication);
    }
}