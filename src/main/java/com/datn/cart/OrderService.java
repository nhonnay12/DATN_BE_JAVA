package com.datn.cart;

import com.datn.models.entity.*;
import com.datn.models.exception.AppException;
import com.datn.models.exception.ErrorCode;
import com.datn.repository.CartRepository;
import com.datn.repository.OrderRepository;
import com.datn.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderService {

    CartRepository cartRepository;
    OrderRepository orderRepository;
    private final UserRepository userRepository;

    // Tạo đơn hàng mới từ giỏ hàng
    public Order createOrderFromCart(CreateOrderRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String userId = user.getId(); // Lấy userId từ SecurityContextHolder
        log.info("Searching for Cart with userId: {} and status: {}", userId, "ACTIVE");

        // Tìm giỏ hàng đang hoạt động của người dùng
        Cart cart = cartRepository.findByUserIdAndStatus(userId, "ACTIVE")
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXITS));
        log.info("Found Cart: {}", cart);

        // Kiểm tra xem Order đã tồn tại hay chưa
        Order order = orderRepository.findByCart(cart).orElse(null);

        if (order != null) {
            // Nếu Order đã tồn tại, cập nhật lại các thông tin cần thiết
            log.info("Order already exists for the cart. Updating order.");
            order.setFirstname(request.getFirstname());
            order.setLastname(request.getLastname());
            order.setCountry(request.getCountry());
            order.setAddress(request.getAddress());
            order.setTown(request.getTown());
            order.setState(request.getState());
           // order.setPostCode(request.getPostCode());
            order.setEmail(request.getEmail());
            order.setPhone(request.getPhone());
            order.setNote(request.getNote());
            order.setPaymentmethod(request.getPaymentmethod());
            order.setTotalPrice(cart.getTotalPrice());
            order.setStatus(OrderStatus.PENDING); // Cập nhật trạng thái
        } else {
            // Nếu Order chưa tồn tại, tạo mới
            log.info("Creating a new order for the cart.");
            order = new Order();
            order.setCart(cart); // Liên kết Cart
            order.setUserId(userId);
            order.setFirstname(request.getFirstname());
            order.setLastname(request.getLastname());
            order.setCountry(request.getCountry());
            order.setAddress(request.getAddress());
            order.setTown(request.getTown());
            order.setState(request.getState());
          //  order.setPostCode(request.getPostCode());
            order.setEmail(request.getEmail());
            order.setPhone(request.getPhone());
            order.setNote(request.getNote());
            order.setPaymentmethod(request.getPaymentmethod());
            order.setTotalPrice(cart.getTotalPrice());
            order.setStatus(OrderStatus.PENDING); // Trạng thái ban đầu
        }

        // Lưu đơn hàng
        return orderRepository.save(order);
    }

    public Order updateOrderStatus(String orderId, String responseCode) {
        // Tìm Order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXIST));

        // Xử lý trạng thái dựa trên responseCode
        switch (responseCode) {
            case "00": // Thanh toán thành công
                order.setStatus(OrderStatus.COMPLETED);
                break;
            case "99": // Lỗi không xác định hoặc đang chờ xử lý
                order.setStatus(OrderStatus.PENDING);
                break;
            default: // Các lỗi khác
                order.setStatus(OrderStatus.CANCELLED);
                break;
        }

        // Lưu Order
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
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