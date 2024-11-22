package com.datn.models.mapper;

import com.datn.cart.CartDTO;
import com.datn.cart.CartItemDTO;
import com.datn.models.entity.Cart;
import com.datn.models.entity.CartItem;

import java.util.List;
import java.util.stream.Collectors;

public interface CartMapper {

    public static CartDTO toCartDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setUserId(cart.getUserId());
        cartDTO.setStatus(cart.getStatus());
        cartDTO.setTotalPrice(cart.getTotalPrice());
        cartDTO.setTotalProducts(cart.getTotalProducts());

        // Ánh xạ danh sách CartItem
        List<CartItemDTO> cartItemDTOs = cart.getCartItems().stream()
                .map(CartMapper::toCartItemDTO)
                .collect(Collectors.toList());
        cartDTO.setCartItems(cartItemDTOs);

        return cartDTO;
    }

    public static CartItemDTO toCartItemDTO(CartItem cartItem) {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setId(cartItem.getId());
        cartItemDTO.setProduct(cartItem.getProduct());
        cartItemDTO.setQuantity(cartItem.getQuantity());
        cartItemDTO.setPrice(cartItem.getPrice());
        cartItemDTO.setStatus(cartItem.getStatus());
        return cartItemDTO;
    }
}
