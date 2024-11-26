package com.datn.repository;

import com.datn.entity.Cart;
import com.datn.entity.Order;
import com.datn.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findByCart(Cart cart);
    List<Order> findByUserIdAndStatus(String userId ,OrderStatus status);

}
