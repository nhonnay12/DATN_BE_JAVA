package com.datn.cart;


import com.datn.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListOrderResponse {
    private LocalDateTime orderDate;             // Ngày tạo đơn hàng
    private String paymentMethod;                // Phương thức thanh toán
    private List<OrderDetailResponse> details;   // Danh sách chi tiết đơn hàng
    private Long totalOrderPrice;              // Tổng tiền cả đơn hàng
private OrderStatus orderStatus;
    private String maDonHang;
}
