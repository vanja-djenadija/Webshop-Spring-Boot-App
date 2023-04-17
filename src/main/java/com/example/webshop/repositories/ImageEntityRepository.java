package com.example.webshop.repositories;

import com.example.webshop.models.entities.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageEntityRepository extends JpaRepository<ImageEntity, Integer> {
}