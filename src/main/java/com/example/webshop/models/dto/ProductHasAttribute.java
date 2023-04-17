package com.example.webshop.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductHasAttribute implements Serializable {
    @NotNull
    private Attribute attribute;
    @NotBlank
    private String value;
}