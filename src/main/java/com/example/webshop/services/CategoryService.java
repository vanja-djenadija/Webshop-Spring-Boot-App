package com.example.webshop.services;

import com.example.webshop.models.dto.Category;
import com.example.webshop.models.dto.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();

    List<Category> findAllSubcategories(String name);

    Page<Product> findAllProductsInCategory(Pageable page, String categoryName);
}