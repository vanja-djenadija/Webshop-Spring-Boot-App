package com.example.webshop.models.entities;

import com.example.webshop.base.BaseEntity;
import com.example.webshop.models.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity implements BaseEntity<Integer> {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Basic
    @Column(name = "description", nullable = false, length = 1000)
    private String description;
    @Basic
    @Column(name = "price", nullable = false, precision = 2)
    private BigDecimal price;
    @Basic
    @Column(name = "is_new", nullable = false)
    private Boolean isNew;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('ACTIVE', 'SOLD', 'INACTIVE')")
    private ProductStatus status;
    @Basic
    @Column(name = "location", nullable = false, length = 100)
    private String location;
    @Basic
    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;
    @Basic
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<CommentEntity> comments;
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER) // cascade = Cascade.MERGE was before search method
    @ToString.Exclude
    private List<ProductHasAttributeEntity> attributes;
    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<ImageEntity> images;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_user_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private UserEntity seller;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_user_id", referencedColumnName = "id")
    @ToString.Exclude
    private UserEntity customer;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "product_has_category", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    @ToString.Exclude
    private List<CategoryEntity> categories;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductEntity that = (ProductEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}