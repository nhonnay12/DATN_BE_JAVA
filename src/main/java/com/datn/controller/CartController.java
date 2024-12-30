package com.datn.controller;

import com.datn.dto.request.CartDTO;
import com.datn.service.CartService;
import com.datn.dto.AddToCart;
import com.datn.dto.response.ApiResponse;
import com.datn.entity.Cart;
import com.datn.repository.ProductRepository;
import com.datn.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;


    // post cart api
    @PostMapping("/add")
    public ApiResponse<Void> addToCart(@RequestBody AddToCart addToCartDto) {

        //var context = SecurityContextHolder.getContext();
        //String name = context.getAuthentication().getName();
        //User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        cartService.addItemToCart(addToCartDto.getProduct_id());

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Thêm vào giỏ hàng thành công")
               // .result(cartService.addItemToCart(addToCartDto.getProduct_id()))
                .build();
    }

    // removeItemFromCart
    @PutMapping("/remove")
    public ApiResponse<CartDTO> removeItemFromCart(@RequestBody AddToCart addToCartDto) {


        return ApiResponse.<CartDTO>builder()
                .code(200)
                .message("Remove to cart successfully")
                .result(cartService.removeItemFromCart(addToCartDto.getProduct_id()))
                .build();
        // get list cartitem active
    }

    @PutMapping()
    public ApiResponse<Cart> updateCart(@RequestParam String orderId, @RequestParam String status) {

        return ApiResponse.<Cart>builder()
                .code(200)
                .message("Remove to cart successfully")
                .result(cartService.updateCartStatus(orderId, status))
                .build();
    }
    @GetMapping()
    public ApiResponse<CartDTO> getProductToCart() {

        return ApiResponse.<CartDTO>builder()
                .code(200)
                .message("Remove to cart successfully")
                .result(cartService.getProductToCart())
                .build();
    }

}
// get all cart items for a user
//    @GetMapping()
//    public ResponseEntity<CartDto> getCartItems() {
//        var context = SecurityContextHolder.getContext();
//        String name = context.getAuthentication().getName();
//        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//
//        // get cart items
//        CartDto cartDto = cartService.listCartItems(user);
//        return new ResponseEntity<>(cartDto, HttpStatus.OK);
//    }

// delete a cart item for a user

//    @DeleteMapping("/delete/{product_id}")
//    public ApiResponse<Void> deleteCartItem(@PathVariable("product_id") Long product_id) {
//
//        var context = SecurityContextHolder.getContext();
//        String name = context.getAuthentication().getName();
//        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//        Product product = productRepository.findById(product_id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
//        cartService.deleteCartItem(product, user);
//
//        return ApiResponse.<Void>builder()
//                .code(200)
//                .message("Cart successfully deleted")
//                .build();
//
//    }

//    @DeleteMapping("/deleteAll")
//    public ApiResponse<Void> deleteAllCartItems() {
//        var context = SecurityContextHolder.getContext();
//        String name = context.getAuthentication().getName();
//        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//
//        cartService.deleteAll(user);
//        return ApiResponse.<Void>builder()
//                .code(200)
//                .message("Deleted all cart items.")
//                .build();
//    }