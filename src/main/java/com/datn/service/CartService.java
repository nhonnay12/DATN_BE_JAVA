package com.datn.service;

import com.datn.models.dto.AddToCart;
import com.datn.models.dto.CartDto;
import com.datn.models.entity.Cart;
import com.datn.models.entity.User;

public interface CartService {
    void deleteAll(User user);
    void deleteCartItem(Integer cartItemId, User user);;
    CartDto listCartItems(User user);
    Cart addToCart(AddToCart addToCartDto, User user);
}
