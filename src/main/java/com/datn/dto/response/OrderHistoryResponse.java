package com.datn.dto.response;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class OrderHistoryResponse {
        private LocalDateTime orderDate;             // Ngày tạo đơn hàng
        private String paymentMethod;                // Phương thức thanh toán
        private List<OrderDetailResponse> details;   // Danh sách chi tiết đơn hàng
        private Double totalOrderPrice;              // Tổng tiền cả đơn hàng
    }

