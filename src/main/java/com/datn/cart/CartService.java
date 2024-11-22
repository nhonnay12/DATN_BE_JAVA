package com.datn.cart;

import com.datn.models.entity.Cart;
import com.datn.models.entity.CartItem;
import com.datn.models.entity.Product;
import com.datn.models.entity.User;
import com.datn.models.exception.AppException;
import com.datn.models.exception.ErrorCode;

import com.datn.models.mapper.CartMapper;
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
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;

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
    public CartDTO addItemToCart(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String userId = user.getId(); // lấy userId từ header

        // Tìm giỏ hàng với trạng thái ACTIVE cho người dùng
        Cart cart = cartRepository.findByUserIdAndStatus(userId, "ACTIVE")
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    newCart.setStatus("ACTIVE");  // Tạo giỏ hàng mới với trạng thái ACTIVE
                    return cartRepository.save(newCart);
                });

        // Lọc các CartItem có trạng thái ACTIVE
//        List<CartItem> activeCartItems = cart.getCartItems().stream()
//                .filter(item -> "ACTIVE".equals(item.getStatus()))
//                .collect(Collectors.toList());

        // Lấy thông tin sản phẩm từ Product
        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        double productPrice = product.getPrice();
        int quantity = 1;  // Số lượng mặc định là 1
        long totalPrice = (long) productPrice * quantity;

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng hay chưa
        Optional<CartItem> existingCartItem = cart.getCartItems().stream().filter(cartItem -> cartItem.getProduct().getId().equals(productId) && "ACTIVE".equals(cartItem.getStatus())).findFirst();

        if (existingCartItem.isPresent()) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED_TO_CART);
        } else {
            // Nếu chưa có, thêm mới sản phẩm vào giỏ hàng
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(totalPrice);  // Thiết lập giá cho sản phẩm
            cartItem.setStatus("ACTIVE");
            cart.getCartItems().add(cartItem);
        }

        // Cập nhật tổng giá của giỏ hàng
        cart.setTotalPrice(cart.getCartItems().stream()
                .filter(cartItem -> "ACTIVE".equals(cartItem.getStatus()))
                .mapToLong(CartItem::getPrice)
                .sum());
        //
        cart.setTotalProducts(cart.getCartItems().stream()
                .filter(cartItem -> "ACTIVE".equals(cartItem.getStatus()))
                .mapToInt(CartItem::getQuantity)
                .sum());
        log.info("Cart Items Before: " + cart.getCartItems());
        log.info("Cart Items After: " + cart.getCartItems());
        log.info("Total Products: " + cart.getTotalProducts());
        // lay cartitem - active
        List<CartItem> activeCartItems = cart.getCartItems().stream()
                .filter(cartItem -> "ACTIVE".equals(cartItem.getStatus()))
                .collect(Collectors.toList());
        cart.setCartItems(activeCartItems);

        return CartMapper.toCartDTO(cartRepository.save(cart)); // Trả về DTO thay vì entity
    }

    public CartDTO removeItemFromCart(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String userId = user.getId();

        Cart cart = cartRepository.findByUserIdAndStatus(userId, "ACTIVE")
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXITS));

        // Tìm kiếm sản phẩm trong giỏ hàng
        Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
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

    // Lấy tất cả giỏ hàng của người dùng (bao gồm cả các giỏ hàng đã checkout)
    public List<Cart> getAllCartsByUserId(String userId) {
        return cartRepository.findAllByUserId(userId);
    }

    // get cart to 1 user
    public CartDTO getProductToCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String userId = user.getId(); // lấy userId từ header

        // Tìm giỏ hàng với trạng thái ACTIVE cho người dùng
        Cart cart = cartRepository.findByUserIdAndStatus(userId, "ACTIVE")
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXITS));

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
