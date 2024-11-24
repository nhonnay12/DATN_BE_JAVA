package com.datn.repository;

import com.datn.cart.CartDTO;
import com.datn.cart.OrderHistoryResponse;
import com.datn.models.entity.Cart;
import com.datn.models.entity.Product;
import com.datn.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
//    List<Cart> findAllByUserOrderByCreatedDateDesc(User user);
//    Optional<Cart> findByUserAndProduct( User user, Product product);
//
//    Optional<Cart> findByUser(User user);
//    List<Cart> findAllByUser(User user);
Optional<Cart> findByUserIdAndStatus(String userId, String status);

    List<Cart> findAllByUserId(String userId);
    Cart findByOrderIdAndStatus(String oderId, String status);
}
