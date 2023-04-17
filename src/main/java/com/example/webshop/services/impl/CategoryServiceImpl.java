package com.example.webshop.services.impl;

import com.example.webshop.models.dto.Category;
import com.example.webshop.models.dto.Product;
import com.example.webshop.models.entities.CategoryEntity;
import com.example.webshop.repositories.CategoryEntityRepository;
import com.example.webshop.services.CategoryService;
import com.example.webshop.util.ConversionUtil;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Data
public class CategoryServiceImpl implements CategoryService {

    private final ModelMapper modelMapper;

    private final CategoryEntityRepository repository;

    public CategoryServiceImpl(ModelMapper modelMapper, CategoryEntityRepository repository) {
        this.modelMapper = modelMapper;
        this.repository = repository;
    }

    @Override
    public List<Category> findAll() {
        return repository.findAllRootCategories().stream().map(c -> modelMapper.map(c, Category.class)).toList();
    }

    @Override
    public List<Category> findAllSubcategories(String name) {
        return repository.findSubcategories(name).stream().map(s -> modelMapper.map(s, Category.class)).toList();
    }

    @Override
    public Page<Product> findAllProductsInCategory(Pageable page, String categoryName) {
        CategoryEntity categoryEntity = repository.findByName(categoryName);
        List<Product> products = categoryEntity.getProducts().stream().map(p -> modelMapper.map(p, Product.class)).toList();
        return ConversionUtil.getPage(page, products);
    }
}