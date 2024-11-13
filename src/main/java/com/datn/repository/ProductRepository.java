package com.datn.repository;


import com.datn.models.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    // Tìm sản phẩm theo category_id và phân trang
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Product> findByPublisherId(Long publisherId, Pageable pageable);
}
