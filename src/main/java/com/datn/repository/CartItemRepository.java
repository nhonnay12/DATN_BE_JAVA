package com.datn.repository;

import com.datn.models.entity.Cart;
import com.datn.models.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository  extends JpaRepository<CartItem, String> {
    CartItem findByStatus(String status);
}
