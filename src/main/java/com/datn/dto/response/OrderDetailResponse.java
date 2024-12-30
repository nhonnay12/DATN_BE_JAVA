package com.datn.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    private String productName;    // Tên sản phẩm
    private Integer quantity;      // Số lượng
    private Double price;          // Giá mỗi sản phẩm
    private String linkDrive;
   // private Double totalPrice;     // Tổng tiền (price * quantity)
}
