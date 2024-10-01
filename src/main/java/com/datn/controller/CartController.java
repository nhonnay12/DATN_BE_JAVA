package com.datn.controller;

import com.datn.models.dto.AddToCart;
import com.datn.models.dto.CartDto;
import com.datn.models.dto.CartItemDto;
import com.datn.models.dto.response.ApiResponse;
import com.datn.models.entity.Cart;
import com.datn.models.entity.User;
import com.datn.models.exception.AppException;
import com.datn.models.exception.ErrorCode;
import com.datn.repository.UserRepository;
import com.datn.service.CartService;
import com.datn.service.impl.CartServiceImpl;
import com.datn.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;
@Autowired
private UserRepository userRepository;


    // post cart api
    @PostMapping("/add")
    public ApiResponse<Date> addToCart(@RequestBody AddToCart addToCartDto) {

        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));


        return ApiResponse.<Date>builder()
                .code(200)
                .message("Add cart successfully")
                .result(cartService.addToCart(addToCartDto, user ).getCreatedDate())
                .build();
    }


    // get all cart items for a user
    @GetMapping()
    public ResponseEntity<CartDto> getCartItems() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // get cart items
        CartDto cartDto = cartService.listCartItems(user);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    // delete a cart item for a user

    @DeleteMapping("/delete/{cartItemId}")
    public String deleteCartItem(@PathVariable("cartItemId") Integer itemId) {

        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        cartService.deleteCartItem(itemId, user);

        return "Deleted " + itemId + " from cart.";

    }
@DeleteMapping("/deleteAll")
    public ApiResponse<Void> deleteAllCartItems() {
    var context = SecurityContextHolder.getContext();
    String name = context.getAuthentication().getName();
    User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

    cartService.deleteAll(user);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Deleted all cart items.")
                .build();
}
}