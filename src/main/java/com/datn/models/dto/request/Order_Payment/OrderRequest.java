package com.datn.models.dto.request.Order_Payment;

import com.datn.models.dto.CartDto;
import com.datn.models.dto.CartItemDto;
import com.datn.models.entity.Cart;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    //private String userId;
    //private List<Cart> cartItems;  // Danh sách sản phẩm từ Cart
    private String paymentMethod;
    private String shippingAddress;
   // private Double totalAmount;
}

