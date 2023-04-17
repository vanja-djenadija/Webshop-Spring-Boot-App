package com.example.webshop.repositories;

import com.example.webshop.models.entities.ProductEntity;
import com.example.webshop.models.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductEntityRepository extends JpaRepository<ProductEntity, Integer> {
    Page<ProductEntity> findAllBySeller_IdAndStatus(Pageable page, Integer id, ProductStatus status);

    Page<ProductEntity> findAllByCustomer_Id(Pageable page, Integer id);

    Page<ProductEntity> searchAllByNameContainsIgnoreCase(Pageable page, String name);

    Page<ProductEntity> findAllByStatus(Pageable pageable, ProductStatus status);

    Page<ProductEntity> findAllByStatusAndNameContainingIgnoreCase(Pageable pageable, ProductStatus status, String productName);
}