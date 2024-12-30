package com.datn.service;

import com.datn.dto.request.CreateOrderRequest;
import com.datn.dto.response.ListOrderResponse;
import com.datn.dto.response.OrderDetailResponse;
import com.datn.dto.response.OrderHistoryResponse;
import com.datn.entity.*;

import com.datn.exception.AppException;
import com.datn.exception.ErrorCode;
import com.datn.mapper.ProductMapper;
import com.datn.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    OrderDetailRepo orderDetailRepository;
    private final ProductMapper productMapper;

    public Order createOrderFromCart(CreateOrderRequest request) {
        // Lấy thông tin người dùng từ SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String userId = user.getId(); // Lấy userId từ SecurityContextHolder
        log.info("Searching for Cart with userId: {} and status: {}", userId, "ACTIVE");

        // Tìm giỏ hàng đang hoạt động của người dùng
        Cart cart = cartRepository.findByUserIdAndStatus(user.getId(), "ACTIVE").orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXITS));

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

        for (CartItem cartItem : cart.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order); // Liên kết Order với OrderDetail
            orderDetail.setProduct(cartItem.getProduct()); // Liên kết Product với OrderDetail
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPrice((double)cartItem.getPrice()); // Lưu giá sản phẩm tại thời điểm đặt hàng
            orderDetailRepository.save(orderDetail); // Lưu OrderDetail
        }
        return savedOrder;
    }

    // Order History
    public List<OrderHistoryResponse> getOrderHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String userId = user.getId(); // Lấy userId từ SecurityContextHolder
        // Lấy danh sách đơn hàng đã hoàn thành của người dùng
        List<Order> orders = orderRepository.findByUserIdAndStatus(userId, OrderStatus.COMPLETED);

        // Ánh xạ từ Order sang OrderHistoryResponse
        return orders.stream().map(order -> {
            // Tính tổng giá trị đơn hàng
            Double totalOrderPrice = (double) order.getTotalPrice();

            // Tạo danh sách chi tiết đơn hàng
            List<OrderDetailResponse> detailResponses = order.getOrderDetailList().stream().map(detail -> {
                Product product = detail.getProduct();
                return new OrderDetailResponse(
                        product.getName(),                 // Tên sản phẩm
                        product.getQuantity(),              // Số lượng
                        product.getPrice(),                // Giá mỗi sản phẩm
                        product.getLinkDrive()
                );
            }).collect(Collectors.toList());

            return new OrderHistoryResponse(
                    order.getPaymentDate(),                // Ngày tạo đơn hàng
                    order.getPaymentmethod(),      // Phương thức thanh toán
                    detailResponses,                    // Danh sách chi tiết đơn hàng
                    totalOrderPrice                     // Tổng giá trị đơn hàng
            );
        }).collect(Collectors.toList());
    }

    public List<ListOrderResponse> getallOrder() {
        // Lấy danh sách đơn hàng
        List<Order> orders = orderRepository.findAll();

        // Ánh xạ từ Order sang lisOrderResponse
        return orders.stream().map(order -> {
            // Tính tổng giá trị đơn hàng
            Long totalOrderPrice =  order.getTotalPrice();

            // Tạo danh sách chi tiết đơn hàng
            List<OrderDetailResponse> detailResponses = order.getOrderDetailList().stream().map(detail -> {
                Product product = detail.getProduct();
                return new OrderDetailResponse(
                        product.getName(),                 // Tên sản phẩm
                        product.getQuantity(),              // Số lượng
                        product.getPrice(),                // Giá mỗi sản phẩm
                        product.getLinkDrive()
                );
            }).collect(Collectors.toList());
            String maDonHang =order.getVnp_TxnRef();

            return new ListOrderResponse(
                    order.getPaymentDate(),                // Ngày tạo đơn hàng
                    order.getPaymentmethod(),      // Phương thức thanh toán
                    detailResponses,                    // Danh sách chi tiết đơn hàng
                    totalOrderPrice,                   // Tổng giá trị đơn hàng
                    order.getStatus(),
                    maDonHang

            );
        }).collect(Collectors.toList());
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
        // viet them vao day update order detail
        return null;
    }
//    public OrderDetailDto  updateOrderDetail(){
//
//    }

//public OrderHistoryResponse getOrderHistory(String orderId) {
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    User user = userRepository.findByUsername(authentication.getName())
//            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//    String userId = user.getId(); // Lấy userId từ SecurityContextHolder
//        OrderHistoryResponse orderHistoryResponse = new OrderHistoryResponse();
//        List<Order> Orders = orderRepository.findByUserIdAndStatus(userId, OrderStatus.COMPLETED);
//        for(Order order : Orders) {
//            orderHistoryResponse.setProduct(order.getCart().getCartItems()
//                    .stream()
//                    .filter(cartItem -> cartItem.getProduct()).collect(Collectors.toCollection()));
//        }
//}

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