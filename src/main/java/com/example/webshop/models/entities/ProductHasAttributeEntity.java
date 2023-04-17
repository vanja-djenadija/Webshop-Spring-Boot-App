package com.example.webshop.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "product_has_attribute")
@IdClass(ProductHasAttributeKey.class)
public class ProductHasAttributeEntity {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private AttributeEntity attribute;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private ProductEntity product;

    @Basic
    @Column(name = "value", nullable = false, length = 100)
    private String value;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductHasAttributeEntity that = (ProductHasAttributeEntity) o;
        return attribute != null && Objects.equals(attribute, that.attribute);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}