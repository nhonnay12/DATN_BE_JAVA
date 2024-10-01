package com.datn.models.dto.response.order_payment;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price;  // Giá của sản phẩm tại thời điểm mua
}
