package com.datn.repository;

import com.datn.models.entity.Cart;
import com.datn.models.entity.Product;
import com.datn.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findAllByUserOrderByCreatedDateDesc(User user);

    Optional<Cart> findByUserAndProduct(User user, Product product);

    List<Cart> findAllByUser(User user);
}
