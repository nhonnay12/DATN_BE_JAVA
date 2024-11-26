package com.datn.repository;


import com.datn.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByName(String name);

    // Tìm sản phẩm theo category_id và phân trang

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Product> findByAuthorId(Long authorId, Pageable pageable);
    Page<Product> findByPublisherId(Long publisherId, Pageable pageable);
    Page<Product> findByUserId(String userId, Pageable pageable);
}
