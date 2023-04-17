package com.example.webshop.repositories;

import com.example.webshop.models.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryEntityRepository extends JpaRepository<CategoryEntity, Integer> {
    CategoryEntity findByName(String name);

    @Query("select s from CategoryEntity c inner join CategoryEntity s where c.name=s.parentCategory.name and c.name=:name")
    List<CategoryEntity> findSubcategories(@Param("name") String name);

    @Query("select c from CategoryEntity c where c.parentCategory=null")
    List<CategoryEntity> findAllRootCategories();

}