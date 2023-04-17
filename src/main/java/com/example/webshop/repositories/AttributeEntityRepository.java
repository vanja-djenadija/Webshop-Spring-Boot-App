package com.example.webshop.repositories;

import com.example.webshop.models.entities.AttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeEntityRepository extends JpaRepository<AttributeEntity, Integer> {
}