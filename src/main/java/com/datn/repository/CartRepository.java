package com.datn.repository;

import com.datn.entity.Cart;
import com.datn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

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
    Cart findByUser(User user);
}
