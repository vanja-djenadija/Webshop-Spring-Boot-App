package com.example.webshop.models.requests;

import com.example.webshop.models.dto.Image;
import com.example.webshop.models.dto.ProductHasAttribute;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class ProductRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private BigDecimal price;
    @NotNull
    private Boolean isNew;
    @NotBlank
    private String location;
    @NotNull
    private Timestamp createDate;
    @NotNull
    private Integer quantity;
    @NotEmpty
    private List<@NotNull ProductHasAttribute> attributes;
    private List<Image> images;
    @NotNull
    private Integer sellerId;
    @NotEmpty
    private List<@NotNull CategoryRequest> categories;
}