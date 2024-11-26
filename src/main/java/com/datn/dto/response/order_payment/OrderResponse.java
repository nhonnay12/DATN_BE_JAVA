package com.datn.dto.response.order_payment;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long orderId;
    private String orderStatus;
    private LocalDateTime orderDate;
    private List<OrderDetailResponse> items;  // Thông tin các sản phẩm trong đơn hàng
    private Double totalAmount;
    private String paymentMethod;
    private String shippingAddress;

}

