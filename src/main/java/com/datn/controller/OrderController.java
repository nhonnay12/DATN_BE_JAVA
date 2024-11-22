package com.datn.controller;

import com.datn.cart.CreateOrderRequest;

import com.datn.cart.OrderService;
import com.datn.models.dto.request.Order_Payment.OrderRequest;
import com.datn.models.dto.response.ApiResponse;
import com.datn.models.dto.response.order_payment.OrderResponse;

import com.datn.models.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ApiResponse<Order> createOrder(@RequestBody CreateOrderRequest request) {
        ApiResponse<Order> apiResponse = ApiResponse.<Order>builder()
                .code(200)
                .message("Create Order Success")
                .result(orderService.createOrderFromCart(request))
                .build();
        return apiResponse;
    }
    @PutMapping
    public ApiResponse<Order> updateStatus(@RequestParam String orderId, @RequestParam String responseCode) {
        ApiResponse<Order> apiResponse = ApiResponse.<Order>builder()
                .code(200)
                .message("Update Order Success")
                .result(orderService.updateOrderStatus(orderId, responseCode))
                .build();
        return apiResponse;
    }
    @GetMapping
    public ApiResponse<List<Order>> getall() {
        ApiResponse<List<Order>> apiResponse = ApiResponse.<List<Order>>builder()
                .code(200)
                .message("Getall Order Success")
                .result(orderService.getAllOrders())
                .build();
        return apiResponse;
    }
}
