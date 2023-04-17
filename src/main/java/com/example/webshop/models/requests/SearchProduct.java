package com.example.webshop.models.requests;

import com.example.webshop.models.dto.ProductHasAttribute;
import lombok.Data;

import java.util.List;

@Data
public class SearchProduct {
    private String categoryName;
    private List<ProductHasAttribute> categoryAttributes;
    private Integer priceFrom;
    private Integer priceTo;
    private String location;
}