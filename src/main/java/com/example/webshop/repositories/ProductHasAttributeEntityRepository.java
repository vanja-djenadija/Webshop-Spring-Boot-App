package com.example.webshop.repositories;

import com.example.webshop.models.entities.ProductHasAttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductHasAttributeEntityRepository extends JpaRepository<ProductHasAttributeEntity, Integer> {
}