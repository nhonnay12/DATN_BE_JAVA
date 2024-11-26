package com.datn.controller;

import com.datn.cart.CreateOrderRequest;

import com.datn.cart.OrderHistoryResponse;
import com.datn.cart.OrderService;
import com.datn.dto.response.ApiResponse;

import com.datn.entity.Order;
import com.datn.entity.OrderStatus;
import com.datn.entity.User;
import com.datn.service.impl.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private EmailServiceImpl emailServiceImpl;

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
    public ApiResponse<Void> updateStatus(
            @RequestParam String orderId,
            @RequestParam String  vnp_TxnRef,
            @RequestParam String transactionId) {
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(200)
                .message("Update Order Success")

                .build();
        orderService.updateOrderStatus(orderId,vnp_TxnRef, OrderStatus.COMPLETED, transactionId);
        return apiResponse;
    }
    @GetMapping("/history")
    public ApiResponse<List<OrderHistoryResponse>> getHistoryOrder() {
        ApiResponse<List<OrderHistoryResponse>> apiResponse = ApiResponse.<List<OrderHistoryResponse>>builder()
                .code(200)
                .message("Getall Order Success")
                .result(orderService.getOrderHistory())
                .build();
        return apiResponse;
    }

    @PostMapping("/send-email")
    public ApiResponse<User> sendOrderConfirmationEmail(@RequestParam String cartId) {

     return ApiResponse.<User>builder()
             .code(200)
             .message("Send Order Confirmation Email Success")
             .result(emailServiceImpl.sendEmailProduct(cartId))
             .build()  ;
    }


//    // API để lấy lịch sử đơn hàng với phân trang
//    @GetMapping("/{userId}/page")
//    public Page<OrderHistoryResponse> getOrderHistoryWithPagination( Pageable pageable) {
//        return orderService.getOrderHistoryByUserIdWithPagination( pageable);
//    }
}
