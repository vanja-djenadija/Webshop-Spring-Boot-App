package com.example.webshop.models.dto;

import com.example.webshop.models.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attribute implements Serializable {
    private Integer id;
    private String name;
    private Type type;
}