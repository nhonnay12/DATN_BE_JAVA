package com.datn.dto;

import com.datn.entity.Order;
import com.datn.entity.Product;

public class OrderDetailDto {
    private Long id;
    private Order order;
    private Product product;
    private Integer quantity;
    private Double price; // Giá mỗi sản phẩm tại thời điểm đặt hàng
}
