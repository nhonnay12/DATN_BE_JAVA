//package com.datn.service.impl;
//
//import com.datn.models.dto.CartDto;
//import com.datn.models.dto.CartItemDto;
//import com.datn.models.dto.request.Order_Payment.OrderRequest;
//import com.datn.models.dto.response.order_payment.OrderDetailResponse;
//import com.datn.models.dto.response.order_payment.OrderResponse;
//import com.datn.models.entity.*;
//import com.datn.models.exception.AppException;
//import com.datn.models.exception.ErrorCode;
//import com.datn.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class OrderService {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private OrderDetailRepository orderDetailRepository;
//
//    @Autowired
//    private CartRepository cartRepository;
//
//    public OrderResponse createOrder(OrderRequest request) {
//        // Lấy thông tin người dùng từ userId
//        var context = SecurityContextHolder.getContext();
//        String name = context.getAuthentication().getName();
//        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//
//        // Lấy thông tin từ giỏ hàng
//        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
//        if (cartList.isEmpty()) {
//            throw new AppException(ErrorCode.CART_EMPTY);
//        }
//
//        List<OrderDetail> orderDetails = new ArrayList<>();
//        double totalCost = 0;
//
//        // Tạo chi tiết đơn hàng từ giỏ hàng
//        // Tạo chi tiết đơn hàng từ giỏ hàng
//        for (Cart cart : cartList) {
//            for (Product product : cart.getProduct()) {
//                // Tạo mới OrderDetail cho mỗi sản phẩm trong giỏ hàng
//                OrderDetail orderDetail = OrderDetail.builder()
//                        .order(null)  // Sẽ được set sau khi lưu Order
//                        .product(product)
//                        .quantity(product.getQuantity())  // Sử dụng số lượng từ giỏ hàng
//                        .price(product.getPrice())  // Lưu giá sản phẩm tại thời điểm mua
//                        .build();
//                orderDetails.add(orderDetail);
//                totalCost += product.getQuantity() * product.getPrice();  // Tính tổng giá trị đơn hàng
//            }
//        }
//
//        // Tạo mới đối tượng Order
//        Order order = Order.builder()
//                .user(user)
//                .orderDate(LocalDateTime.now())
//                .totalAmount(totalCost)
//                .paymentMethod(request.getPaymentMethod())
//                .shippingAddress(request.getShippingAddress())
//                .orderStatus("PENDING")
//                .build();
//
//        // Lưu thông tin đơn hàng vào cơ sở dữ liệu
//        order = orderRepository.save(order);
//
//        // Gán Order vào các OrderDetail và lưu vào cơ sở dữ liệu
//        for (OrderDetail orderDetail : orderDetails) {
//            orderDetail.setOrder(order);
//            orderDetailRepository.save(orderDetail);
//        }
//
//        // Tạo danh sách OrderDetailResponse
//        List<OrderDetailResponse> detailResponses = orderDetails.stream()
//                .map(orderDetail -> OrderDetailResponse.builder()
//                        .productId(orderDetail.getProduct().getId())
//                        .productName(orderDetail.getProduct().getName())
//                        .quantity(orderDetail.getQuantity())
//                        .price(orderDetail.getPrice())
//                        .build())
//                .collect(Collectors.toList());
//
//        // Tạo response cho Order
//        OrderResponse response = OrderResponse.builder()
//                .orderId(order.getId())
//                .orderDate(order.getOrderDate())
//                .totalAmount(order.getTotalAmount())
//                .paymentMethod(order.getPaymentMethod())
//                .shippingAddress(order.getShippingAddress())
//                .orderStatus(order.getOrderStatus())
//                .items(detailResponses)  // Set chi tiết đơn hàng vào response
//                .build();
//
//        return response;
//    }
//
//    // Lấy thông tin chi tiết đơn hàng
//    public List<OrderDetailResponse> getOrderDetails(Long orderId) {
//        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
//        return orderDetails.stream()
//                .map(orderDetail -> OrderDetailResponse.builder()
//                        .productId(orderDetail.getProduct().getId())
//                        .productName(orderDetail.getProduct().getName())
//                        .quantity(orderDetail.getQuantity())
//                        .price(orderDetail.getPrice())
//                        .build())
//                .collect(Collectors.toList());
//    }
//}
