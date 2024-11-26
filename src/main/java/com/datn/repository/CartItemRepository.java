package com.datn.repository;

import com.datn.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository  extends JpaRepository<CartItem, String> {
    CartItem findByStatus(String status);
    //List<CartItem> findByOrderIdAndStatus( String orderId, String status);
    List<CartItem> findByCartIdAndStatus( String cartId, String status);
    List<CartItem> findByCartId(String carId);
}
