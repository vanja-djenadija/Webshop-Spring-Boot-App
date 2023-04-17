package com.example.webshop.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {
    private Integer id;
    private String name;
    private List<Attribute> attributes;

    private List<Category> subcategories;
}