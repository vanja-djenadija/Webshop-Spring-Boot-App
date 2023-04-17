package com.example.webshop.services;

import com.example.webshop.exceptions.NotFoundException;
import com.example.webshop.models.dto.Comment;
import com.example.webshop.models.dto.Product;
import com.example.webshop.models.entities.ProductEntity;
import com.example.webshop.models.requests.SearchProduct;
import com.example.webshop.models.requests.CommentRequest;
import com.example.webshop.models.requests.ProductRequest;
import com.example.webshop.models.requests.PurchaseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ProductService {

    Page<Product> findAll(Pageable page);

    Page<Product> findAllActive(Pageable page, String productName);

    Product findById(Integer id);

    Page<Product> findAllByCategory(Pageable page, String categoryName);

    Product insert(ProductRequest productRequest, Authentication authentication);

    Product delete(Integer id, Authentication authentication) throws NotFoundException;

    Comment comment(CommentRequest commentRequest, Authentication authentication);

    Product purchase(Integer id, PurchaseRequest purchaseRequest, Authentication authentication);

    Page<Product> search(Pageable page, SearchProduct searchProduct);
}