package com.datn.repository;

import com.datn.models.entity.Cart;
import com.datn.models.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findByCart(Cart cart);
}
