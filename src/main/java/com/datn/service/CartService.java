package com.datn.service;

import com.datn.dto.request.CartDTO;
import com.datn.entity.Cart;
import com.datn.entity.CartItem;
import com.datn.entity.Product;
import com.datn.entity.User;
import com.datn.exception.AppException;
import com.datn.exception.ErrorCode;

import com.datn.mapper.CartMapper;
import com.datn.repository.CartItemRepository;
import com.datn.repository.CartRepository;
import com.datn.repository.ProductRepository;
import com.datn.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartService {

    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    ProductRepository productRepository;
    private final UserRepository userRepository;

    // Thêm sản phẩm vào giỏ hàng
    public Void addItemToCart(Long productId) {
        // Lấy thông tin người dùng từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Tìm giỏ hàng ACTIVE của người dùng
        Cart cart = cartRepository.findByUserIdAndStatus(user.getId(), "ACTIVE").orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setStatus("ACTIVE");
            newCart.setCartItems(new ArrayList<>()); // Đảm bảo danh sách CartItems không bị null
            return newCart;
        });

        // Lấy thông tin sản phẩm từ Product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng hay chưa
        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId) && "ACTIVE".equals(cartItem.getStatus()))
                .findFirst();

        if (existingCartItem.isPresent()) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED_TO_CART);
        }

        // Nếu sản phẩm chưa có, thêm mới vào giỏ hàng
        int quantity = 1; // Số lượng mặc định
        double productPrice = product.getPrice();
        long totalPrice = (long) productPrice * quantity;

        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setPrice(totalPrice);
        newCartItem.setStatus("ACTIVE");

        cart.getCartItems().add(newCartItem);

        // Cập nhật tổng giá và tổng số lượng sản phẩm của giỏ hàng
        cart.setTotalPrice(cart.getCartItems().stream()
                .filter(cartItem -> "ACTIVE".equals(cartItem.getStatus()))
                .mapToLong(CartItem::getPrice)
                .sum());

        cart.setTotalProducts(cart.getCartItems().stream()
                .filter(cartItem -> "ACTIVE".equals(cartItem.getStatus()))
                .mapToInt(CartItem::getQuantity)
                .sum());
        cartRepository.save(cart);
        // Lưu giỏ hàng và trả về DTO
      //  return CartMapper.toCartDTO(cartRepository.save(cart));
        return null;
    }

    public CartDTO removeItemFromCart(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String userId = user.getId();

        Cart cart = cartRepository.findByUserIdAndStatus(user.getId(), "ACTIVE").orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXITS));

        // Tìm kiếm sản phẩm trong giỏ hàng
        Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
               .filter(cartItem -> "ACTIVE".equals(cartItem.getStatus()))
                .findFirst();

        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();

            // Đảm bảo trạng thái của sản phẩm là "ACTIVE" để có thể xóa
            if ("ACTIVE".equals(cartItem.getStatus())) {
                cartItem.setStatus("INACTIVE");
                cart.getCartItems().remove(cartItem);
            } else {
                throw new AppException(ErrorCode.PRODUCT_NOT_ACTIVE);
            }
        } else {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }

        // Cập nhật tổng giá trị và tổng số lượng sản phẩm trong giỏ hàng
        cart.setTotalPrice(cart.getCartItems().stream()
                .filter(item -> "ACTIVE".equals(item.getStatus()))
                .mapToLong(CartItem::getPrice)
                .sum());

        cart.setTotalProducts(cart.getCartItems().stream()
                .filter(item -> "ACTIVE".equals(item.getStatus()))
                .mapToInt(CartItem::getQuantity)
                .sum());

        // Lọc lại các sản phẩm có trạng thái "ACTIVE"
        List<CartItem> cartActive = cart.getCartItems().stream()
                .filter(cartItem -> "ACTIVE".equals(cartItem.getStatus()))
                .collect(Collectors.toList());
        cart.setCartItems(cartActive);

        // Lưu lại giỏ hàng đã thay đổi
        return CartMapper.toCartDTO(cartRepository.save(cart));
    }


    public Cart updateCartStatus(String orderId, String status) {
        Cart cart = cartRepository.findByOrderIdAndStatus(orderId, "ACTIVE");

        // Cập nhật trạng thái giỏ hàng
        cart.setStatus(status);
        cartRepository.save(cart); // Lưu thông tin cập nhật vào cơ sở dữ liệu}
        return cart;
    }

    // Lấy tất cả giỏ hàng của người dùng (bao gồm cả các giỏ hàng đã checkout)
    public List<Cart> getAllCartsByUserId(String userId) {
        return cartRepository.findAllByUserId(userId);
    }

    // get cart to 1 user
    public CartDTO getProductToCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        //String userId = user.getId(); // lấy userId từ header

        // Tìm giỏ hàng với trạng thái ACTIVE cho người dùng
        Cart cart = cartRepository.findByUserIdAndStatus(user.getId(), "ACTIVE").orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
                //.orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXITS));

        // lay cartitem - active
        List<CartItem> activeCartItems = cart.getCartItems().stream()
                .filter(cartItem -> "ACTIVE".equals(cartItem.getStatus()))
                .collect(Collectors.toList());
        cart.setCartItems(activeCartItems);


        return CartMapper.toCartDTO(cartRepository.save(cart));
    }

}

// Cập nhật số lượng cho sản phẩm
//    public Cart updateCartItemQuantity(String productId, int newQuantity) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userId = authentication.getName();
//
//        Cart cart = cartRepository.findByUserIdAndStatus(userId, "ACTIVE")
//                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXITS));
//
//        Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
//                .filter(item -> item.getProductId().equals(productId))
//                .findFirst();
//
//        if (cartItemOptional.isPresent()) {
//            CartItem cartItem = cartItemOptional.get();
//
//            // Nếu số lượng mới là 0, xóa sản phẩm khỏi giỏ hàng
//            if (newQuantity <= 0) {
//                cart.getCartItems().remove(cartItem);
//            } else {
//                cartItem.setQuantity(newQuantity);
//                ProductClientResponse productClientResponse = getProductClientResponse(productId);
//                cartItem.setPrice(productClientResponse.getPrice() * newQuantity);  // Cập nhật giá với số lượng mới
//            }
//        } else {
//            throw new AppException(ErrorCode.PRODUCT_NOT_EXITS);
//        }
//
//        // Cập nhật tổng giá trị của giỏ hàng
//        cart.setTotalPrice(cart.getCartItems().stream()
//                .mapToLong(CartItem::getPrice)
//                .sum());
//
//        return cartRepository.save(cart);
//    }
//    // Xóa sản phẩm khỏi giỏ hàng
//    public CartDTO removeItemFromCart(Long productId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//        String userId = user.getId();
//        Cart cart = cartRepository.findByUserIdAndStatus(userId, "ACTIVE")
//                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXITS));
//
//        Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
//                .filter(item -> item.getProduct().getId().equals(productId))
//                .findFirst();
//
//        if (cartItemOptional.isPresent()) {
//            CartItem cartItem = cartItemOptional.get();
//            cartItem.setStatus("INACTIVE");
//            cart.getCartItems().remove(cartItem);
//        } else {
//            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
//        }
//        cart.setTotalPrice(cart.getCartItems().stream()
//                .filter(item -> "ACTIVE".equals(item.getStatus()))
//                .mapToLong(CartItem::getPrice)
//                .sum());
//        cart.setTotalProducts(cart.getCartItems().stream()
//                .filter(item -> "ACTIVE".equals(item.getStatus()))
//                .mapToInt(CartItem::getQuantity)
//                .sum());
//
//
//        List<CartItem> cartActive = cart.getCartItems().stream()
//                .filter(cartItem -> "ACTIVE".equals(cartItem.getStatus()))
//                .collect(Collectors.toList());
//        cart.setCartItems(cartActive);
//        return CartMapper.toCartDTO(cartRepository.save(cart));
//    }
