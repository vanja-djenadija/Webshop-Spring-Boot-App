package com.example.webshop.models.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductHasAttributeKey implements Serializable {
    private Integer product;
    private Integer attribute;
}