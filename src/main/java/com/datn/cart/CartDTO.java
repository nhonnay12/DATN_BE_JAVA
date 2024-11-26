package com.datn.cart;

import com.datn.entity.User;
import lombok.*;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDTO {
    private String id;
   // private User user;
    private String status;
    private long totalPrice;
    private int totalProducts;
    private List<CartItemDTO> cartItems;
    private String orderId;
}