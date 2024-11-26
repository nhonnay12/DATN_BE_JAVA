package com.datn.dto.request.Order_Payment;

import lombok.*;

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

