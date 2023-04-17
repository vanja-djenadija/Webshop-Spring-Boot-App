package com.example.webshop.models.dto;

import com.example.webshop.models.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean isNew;
    private ProductStatus status;
    private String location;
    private Timestamp createDate;
    private Integer quantity;
    private List<Comment> comments;
    private List<ProductHasAttribute> attributes;
    private List<Image> images;
    private User seller;
    private User customer;
}