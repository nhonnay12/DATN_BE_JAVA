package com.datn.cart;


import com.datn.models.dto.response.ProductResponse;
import com.datn.models.entity.Product;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder

@NoArgsConstructor
public class OrderHistoryResponse {
    private LocalDateTime createdAt;
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private ProductResponse product; // Sản phẩm đã thanh toán

    // Getters and Setters
    // Constructor để sử dụng trong JPQL
    public OrderHistoryResponse(LocalDateTime createdAt, String paymentMethod, LocalDateTime paymentDate, ProductResponse product) {
        this.createdAt = createdAt;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.product = product;
    }
}
