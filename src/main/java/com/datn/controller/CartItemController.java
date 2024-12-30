package com.datn.controller;

import com.datn.service.CartItemService;
import com.datn.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cartItem")
public class CartItemController {


    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    // updatecart item
    @PutMapping()
    public ApiResponse<Void> updateCart(@RequestParam String cartId, @RequestParam String status ) {
        cartItemService.updateCartItemsStatus(cartId, status);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Remove to cart successfully")
                .build();
    }


}