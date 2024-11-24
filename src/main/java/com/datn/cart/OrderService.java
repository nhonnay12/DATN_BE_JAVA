package com.datn.cart;

import com.datn.models.entity.*;
import com.datn.models.exception.AppException;
import com.datn.models.exception.ErrorCode;
import com.datn.repository.CartItemRepository;
import com.datn.repository.CartRepository;
import com.datn.repository.OrderRepository;
import com.datn.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderService {

    CartRepository cartRepository;
    OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    public Order createOrderFromCart(CreateOrderRequest request) {
        // Lấy thông tin người dùng từ SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String userId = user.getId(); // Lấy userId từ SecurityContextHolder
        log.info("Searching for Cart with userId: {} and status: {}", userId, "ACTIVE");

        // Tìm giỏ hàng đang hoạt động của người dùng
        Cart cart = cartRepository.findByUserIdAndStatus(userId, "ACTIVE")
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXITS));

        // Lọc các CartItems có trạng thái là ACTIVE
        cart.setCartItems(cart.getCartItems().stream()
                .filter(cartItem -> "ACTIVE".equals(cartItem.getStatus()))
                .collect(Collectors.toList()));

        // Kiểm tra xem Order đã tồn tại hay chưa, dựa vào Cart
        Order order = orderRepository.findByCart(cart).orElse(null);

        if (order != null) {
            log.info("Order already exists for the cart. Updating order.");
            updateOrderDetails(order, request, cart);
        } else {
            log.info("Creating a new order for the cart.");
            order = new Order();
            order.setCart(cart); // Liên kết Cart
            order.setUserId(userId);
            updateOrderDetails(order, request, cart);
        }
        var savedOrder = orderRepository.save(order);

        // Cập nhật orderId vào giỏ hàng
        cart.setOrderId(savedOrder.getId());
        cartRepository.save(cart);

        // Cập nhật orderId cho từng CartItem
        for (CartItem cartItem : cart.getCartItems()) {
            cartItem.setOrderId(savedOrder.getId());
            cartItemRepository.save(cartItem);
        }
//
//            // Lưu đơn hàng và trả kết quả
        return savedOrder;
//        } catch (Exception ex) {
//            log.error("Error occurred while creating the order: ", ex);
//            throw new RuntimeException("An error occurred while processing your request.");
//        }
    }


    private void updateOrderDetails(Order order, CreateOrderRequest request, Cart cart) {
        order.setFirstname(request.getFirstname());
        order.setLastname(request.getLastname());
        order.setCountry(request.getCountry());
        order.setAddress(request.getAddress());
        order.setTown(request.getTown());
        order.setState(request.getState());
        order.setEmail(request.getEmail());
        order.setPhone(request.getPhone());
        order.setNote(request.getNote());
        order.setPaymentmethod(request.getPaymentmethod());
        order.setTotalPrice(cart.getTotalPrice());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING); // Cập nhật trạng thái
    }

    public Void updateOrderStatus(String orderId, String vnp_TxnRef, OrderStatus status, String transactionId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXIST));

        // Cập nhật trạng thái đơn hàng và lưu mã giao dịch
        order.setStatus(status);
        order.setVnp_TxnRef(vnp_TxnRef);
        order.setTransactionId(transactionId); // Lưu mã giao dịch VNPay
        order.setPaymentDate(LocalDateTime.now()); // Lưu thời gian thanh toán
        orderRepository.save(order); // Lưu thông tin cập nhật vào cơ sở dữ liệu
        return null;
    }



//    // Xử lý thanh toán thành công
//    public void handlePaymentSuccess(String orderId) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
//
//        if (order.getStatus() == OrderStatus.PENDING) {
//            order.setStatus(OrderStatus.PROCESSING);  // Chuyển trạng thái đơn hàng sang PROCESSING
//            orderRepository.save(order);
//        }
//    }
//
//    // Cập nhật trạng thái khi thanh toán hoàn tất
//    public void completeOrder(String orderId) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
//
//        if (order.getStatus() == OrderStatus.PROCESSING) {
//            order.setStatus(OrderStatus.COMPLETED);  // Chuyển trạng thái đơn hàng sang COMPLETED
//            orderRepository.save(order);

//        }
//    }


//    // Lấy đơn hàng theo ID
//    public Order getOrderById(String orderId) {
//        return orderRepository.findById(orderId)
//                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
//    }
//
//    // Lấy tất cả đơn hàng của người dùng
//    public List<Order> getAllOrdersByUserId(String userId) {
//        return orderRepository.findAllByUserId(userId);
//    }
//
//
//    // Hủy đơn hàng
//    public void cancelOrder(String orderId) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
//
//        if (order.getStatus() == OrderStatus.COMPLETED) {
//            throw new AppException(ErrorCode.ORDER_CANNOT_BE_CANCELLED);
//        }
//
//        // Khôi phục số lượng sản phẩm
//        for (CartItem item : order.getCart().getCartItems()) {
//            ProductClientResponse product = getProductClientResponse(item.getProductId());
//            int originalQuantity = product.getQuantity() + item.getQuantity();
//            productClient.updateProductAfterOrderSuccess(item.getProductId(), originalQuantity);
//        }
//
//        // Cập nhật trạng thái đơn hàng thành CANCELLED
//        order.setStatus(OrderStatus.CANCELLED);
//        orderRepository.save(order);
//
//        // Cập nhật trạng thái giỏ hàng (nếu cần)
//        Cart cart = order.getCart();
//        cart.setStatus("ACTIVE");
//        cartRepository.save(cart);
//    }
//
//
//    // Cập nhật trạng thái của đơn hàng
//    public Order updateOrderStatus(String orderId, OrderStatus newStatus) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
//
//        order.setStatus(newStatus);
//        return orderRepository.save(order);
//    }
}