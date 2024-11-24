package com.datn.repository;

import com.datn.cart.OrderHistoryResponse;
import com.datn.models.entity.Cart;
import com.datn.models.entity.Order;
import com.datn.models.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findByCart(Cart cart);
    List<Order> findByUserIdAndStatus(String userId ,OrderStatus status);

}
