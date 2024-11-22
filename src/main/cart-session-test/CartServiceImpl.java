//package com.datn.service.impl;
//
//import com.datn.models.dto.AddToCart;
//import com.datn.models.dto.CartDto;
//import com.datn.models.dto.CartItemDto;
//import com.datn.models.entity.Cart;
//import com.datn.models.entity.Product;
//import com.datn.models.entity.User;
//import com.datn.models.exception.AppException;
//import com.datn.models.exception.ErrorCode;
//import com.datn.repository.CartRepository;
//import com.datn.repository.ProductRepository;
//import com.datn.service.CartService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class CartServiceImpl implements CartService {
//    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);
//    @Autowired
//    ProductRepository productRepository;
//
//    @Autowired
//    CartRepository cartRepository;
//
//    @Override
//    public String addToCart(AddToCart addToCartDto, User user) {
//        Product product = productRepository.findById(addToCartDto.getProduct_id())
//                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
//        // Tìm giỏ hàng hiện tại của người dùng
//        Cart userCart = cartRepository.findByUser(user).orElseGet(() -> {
//            // Nếu chưa có giỏ hàng, tạo giỏ hàng mới
//            Cart newCart = new Cart();
//            newCart.setUser(user);
//            newCart.setCreatedDate(new Date());
//            newCart.setProduct(new ArrayList<>()); // Khởi tạo danh sách sản phẩm
//            cartRepository.save(newCart);
//            return newCart;
//        });
//
//        // Kiểm tra nếu sản phẩm đã có trong giỏ hàng
//        boolean productExists = userCart.getProduct().stream()
//                .anyMatch(p -> p.getId().equals(addToCartDto.getProduct_id()));
//
//        if (productExists) {
//            return "Sản phẩm đã có trong giỏ hàng";
//        } else {
//            // Thêm sản phẩm vào giỏ hàng
//
//            userCart.getProduct().add(product);
//            cartRepository.save(userCart);
//            return "Thêm vào giỏ hàng thành công";
//        }
//    }
//
//
//    @Override
//    public CartDto listCartItems(User user) {
//        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
//
//        List<CartItemDto> cartItems = new ArrayList<>();
//        double totalCost = 0;
//        double totalProduct = 0;
//        Integer cartId = null; // Biến để lưu id của giỏ hàng
//
//        for (Cart cartItem : cartList) {
//            // Lấy id từ giỏ hàng (giả sử giỏ hàng đầu tiên là giỏ hàng chính)
//            if (cartId == null) {
//                cartId = cartItem.getId();
//            }
//
//            // Tạo CartItemDto từ Cart, trong đó bao gồm danh sách sản phẩm
//            CartItemDto cartItemDto = new CartItemDto(cartItem);
//            for (Product product : cartItemDto.getProduct()) { // Duyệt qua từng sản phẩm
//                totalCost += product.getQuantity() * product.getPrice();
//            totalProduct += product.getQuantity();
//            }
//            cartItems.add(cartItemDto);
//        }
//
//        // Sử dụng builder hoặc setter để khởi tạo CartDto
//        CartDto cartDto = CartDto.builder()
//                .id(cartId) // Gán id của giỏ hàng vào CartDto
//                .cart(cartItems)
//                .totalPrice(totalCost)
//                .totalProduct(totalProduct)
//                .build();
//
//        return cartDto;
//    }
//
//    @Override
//    public void deleteCartItem(Product product, User user) {
//        // the item id belongs to user
//
//        Cart optionalCart = cartRepository.findByUserAndProduct(user, product).orElseThrow(() -> new AppException(ErrorCode.PRO_NOT_EXIST_IN_CART));
//
//        cartRepository.delete(optionalCart);
//    }
//
//    @Override
//    public void deleteAll(User user) {
//        List<Cart> cart = cartRepository.findAllByUser(user);
//        cartRepository.deleteAll(cart);
//    }
//}
