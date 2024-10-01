package com.datn.controller;

import com.datn.models.dto.request.Order_Payment.OrderRequest;
import com.datn.models.dto.response.ApiResponse;
import com.datn.models.dto.response.order_payment.OrderResponse;
import com.datn.models.entity.Order;
import com.datn.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ApiResponse<OrderResponse> getOrder(@RequestBody OrderRequest orderRequest) {
        ApiResponse<OrderResponse> apiResponse = ApiResponse.<OrderResponse>builder()
                .code(200)
                .message("sucess")
                .result(orderService.createOrder(orderRequest))
                .build();
        return apiResponse;
    }
}
