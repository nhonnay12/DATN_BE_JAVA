package com.datn.service;

import com.datn.models.dto.request.CartItem;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    List<CartItem> cartItemList = new ArrayList<>();

    public void addCartItem(CartItem cartItem) {
        cartItemList.add(cartItem);
    }

    public void removeCartItem(Long productId) {
        cartItemList.removeIf(cartItem -> cartItem.getProduct().getId() == productId);
    }
    public List<CartItem> getCartItemList() {
        return cartItemList;
    }
    public void clearCartItemList() {
        cartItemList.clear();
    }
}
