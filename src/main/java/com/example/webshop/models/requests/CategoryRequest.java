package com.example.webshop.models.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotNull
    private Integer id;
    @NotEmpty
    private String name;
}