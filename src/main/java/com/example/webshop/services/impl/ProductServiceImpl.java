package com.example.webshop.services.impl;

import com.example.webshop.exceptions.BadRequestException;
import com.example.webshop.exceptions.NotFoundException;
import com.example.webshop.exceptions.UnauthorizedException;
import com.example.webshop.models.dto.Comment;
import com.example.webshop.models.dto.JWTUser;
import com.example.webshop.models.dto.Product;
import com.example.webshop.models.dto.ProductHasAttribute;
import com.example.webshop.models.entities.*;
import com.example.webshop.models.enums.ProductStatus;
import com.example.webshop.models.requests.*;
import com.example.webshop.repositories.*;
import com.example.webshop.services.CategoryService;
import com.example.webshop.services.ProductService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private static final String LOG_MESSAGE_FORMAT = "%s %d by user(id) %d";

    private final ModelMapper modelMapper;
    private final ProductEntityRepository repository;
    private final CommentEntityRepository commentRepository;
    private final UserEntityRepository userRepository;

    private final CategoryEntityRepository categoryRepository;
    private final ImageEntityRepository imageRepository;

    private final ProductHasAttributeEntityRepository productHasAttributeRepository;
    private final CategoryService categoryService;
    @PersistenceContext
    private EntityManager entityManager;

    public ProductServiceImpl(ModelMapper modelMapper, ProductEntityRepository repository, CommentEntityRepository commentRepository, UserEntityRepository userRepository, CategoryEntityRepository categoryRepository, ImageEntityRepository imageRepository, ProductHasAttributeEntityRepository productHasAttributeRepository, CategoryService categoryService) {
        this.modelMapper = modelMapper;
        this.repository = repository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.productHasAttributeRepository = productHasAttributeRepository;
        this.categoryService = categoryService;
    }


    @Override
    public Page<Product> findAll(Pageable page) {
        return repository.findAll(page).map(e -> modelMapper.map(e, Product.class));
    }

    @Override
    public Page<Product> findAllActive(Pageable page, String productName) {
        if (productName == null || productName.isEmpty())
            return repository.findAllByStatus(page, ProductStatus.ACTIVE).map(e -> modelMapper.map(e, Product.class));
        return repository.findAllByStatusAndNameContainingIgnoreCase(page, ProductStatus.ACTIVE, productName).map(e -> modelMapper.map(e, Product.class));
    }

    @Override
    public Product findById(Integer id) {
        return modelMapper.map(repository.findById(id).orElseThrow(NotFoundException::new), Product.class);
    }

    @Override
    public Page<Product> findAllByCategory(Pageable page, String categoryName) {
        return categoryService.findAllProductsInCategory(page, categoryName);
    }

    @Override
    public Product insert(ProductRequest productRequest, Authentication authentication) {
        ProductEntity productEntity = modelMapper.map(productRequest, ProductEntity.class);
        productEntity.setId(null);
        productEntity.setStatus(ProductStatus.ACTIVE);
        productEntity.setCategories(new ArrayList<>());
        for (CategoryRequest categoryRequest : productRequest.getCategories()) {
            CategoryEntity categoryEntity = categoryRepository.findById(categoryRequest.getId()).orElseThrow(NotFoundException::new);
            productEntity.getCategories().add(categoryEntity);
            categoryEntity.getProducts().add(productEntity);
        }
        productEntity = repository.saveAndFlush(productEntity);
        for (ImageEntity image : productEntity.getImages()) {
            image.setId(null);
            image.setProduct(productEntity);
            imageRepository.saveAndFlush(image);
        }
        for (ProductHasAttributeEntity pa : productEntity.getAttributes()) {
            pa.setProduct(productEntity);
            productHasAttributeRepository.saveAndFlush(pa);
        }
        JWTUser user = (JWTUser) authentication.getPrincipal();
        Logger.getLogger(getClass()).info(String.format(LOG_MESSAGE_FORMAT, "INSERT PRODUCT", productEntity.getId(), user.getId()));
        return modelMapper.map(productEntity, Product.class);
    }

    /**
     * Not deleting from db, just setting status to INACTIVE.
     * Only a logged-in user can delete its own products.
     */
    @Override
    public Product delete(Integer id, Authentication authentication) throws NotFoundException {
        ProductEntity productEntity = repository.findById(id).orElseThrow(NotFoundException::new);
        JWTUser user = (JWTUser) authentication.getPrincipal();
        if (!user.getId().equals(productEntity.getSeller().getId()))
            throw new UnauthorizedException();
        if (productEntity.getStatus() != ProductStatus.ACTIVE)
            throw new BadRequestException();
        productEntity.setStatus(ProductStatus.INACTIVE);
        Logger.getLogger(getClass()).info(String.format(LOG_MESSAGE_FORMAT, "DELETE PRODUCT", productEntity.getId(), user.getId()));
        return modelMapper.map(repository.saveAndFlush(productEntity), Product.class);
    }

    @Override
    public Comment comment(CommentRequest commentRequest, Authentication authentication) {
        CommentEntity commentEntity = modelMapper.map(commentRequest, CommentEntity.class);
        commentEntity.setId(null);
        commentEntity = commentRepository.saveAndFlush(commentEntity);
        entityManager.refresh(commentEntity);
        JWTUser user = (JWTUser) authentication.getPrincipal();
        Logger.getLogger(getClass()).info(String.format(LOG_MESSAGE_FORMAT, "COMMENT PRODUCT", commentEntity.getProduct().getId(), user.getId()));
        return modelMapper.map(commentEntity, Comment.class);
    }

    // TODO: Check once more
    @Override
    public Product purchase(Integer id, PurchaseRequest purchaseRequest, Authentication authentication) {
        ProductEntity product = repository.findById(id).orElseThrow(NotFoundException::new);
        UserEntity userEntity = userRepository.findById(purchaseRequest.getCustomerId()).orElseThrow(NotFoundException::new);
        if (product.getCustomer() != null || !product.getStatus().equals(ProductStatus.ACTIVE))
            throw new BadRequestException();
        if (product.getSeller().getId().equals(purchaseRequest.getCustomerId()))
            throw new UnauthorizedException();
        JWTUser user = (JWTUser) authentication.getPrincipal();
        if (user.getId().equals(product.getSeller().getId()))
            throw new UnauthorizedException();
        product.setCustomer(userEntity);
        product.setStatus(ProductStatus.SOLD);
        Logger.getLogger(getClass()).info(String.format(LOG_MESSAGE_FORMAT, "PURCHASE PRODUCT", product.getId(), user.getId()));
        return modelMapper.map(repository.saveAndFlush(product), Product.class);
    }

    // TODO!!!

    @Override
    public Page<Product> search(Pageable page, SearchProduct searchProduct) {
        System.err.println(searchProduct);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> query = cb.createQuery(ProductEntity.class);
        Root<ProductEntity> root = query.from(ProductEntity.class);
        Join<ProductEntity, CategoryEntity> categoryJoin = root.join("categories", JoinType.LEFT);
        Join<ProductEntity, ProductHasAttributeEntity> attributeJoin = root.join("attributes");
        List<Predicate> predicates = new ArrayList<>();

        if (searchProduct.getCategoryName() != null)
            predicates.add(cb.equal(categoryJoin.get("name"), searchProduct.getCategoryName()));

        if (searchProduct.getLocation() != null)
            predicates.add(cb.equal(root.get("location"), searchProduct.getLocation()));

        if (searchProduct.getPriceFrom() != null && searchProduct.getPriceTo() != null)
            predicates.add(cb.and(
                    cb.greaterThanOrEqualTo(root.get("price"), searchProduct.getPriceFrom()),
                    cb.lessThanOrEqualTo(root.get("price"), searchProduct.getPriceTo())));

        List<ProductHasAttribute> categoryAttributes = searchProduct.getCategoryAttributes();
        System.err.println(categoryAttributes);
        if (categoryAttributes != null && !categoryAttributes.isEmpty()) {
            for (ProductHasAttribute attribute : categoryAttributes) {
                predicates.add(cb.like(attributeJoin.get("value"), attribute.getValue())
                        /*cb.and(
                                cb.equal(attributeJoin.get("attribute").get("id"), attribute.getAttribute().getId()),
                                cb.equal(attributeJoin.get("value"), attribute.getValue())
                        )*/
                );
            }
        }

        query.select(root).where(predicates.toArray(new Predicate[0]));

        TypedQuery<ProductEntity> typedQuery = entityManager.createQuery(query);
        List<ProductEntity> productEntities = typedQuery.getResultList();
        List<Product> products = productEntities.stream().map(e -> modelMapper.map(e, Product.class)).toList();

        return new PageImpl<>(products, page, products.size());
    }

}