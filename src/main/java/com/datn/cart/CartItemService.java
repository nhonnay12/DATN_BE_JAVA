package com.datn.cart;

import com.datn.entity.CartItem;
import com.datn.exception.AppException;
import com.datn.exception.ErrorCode;
import com.datn.repository.CartItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    private static final Logger log = LoggerFactory.getLogger(CartItemService.class);
    @Autowired
    private CartItemRepository cartItemRepository;

    public Void updateCartItemsStatus(String cartId, String status) {
        List<CartItem> cartItems = cartItemRepository.findByCartIdAndStatus(cartId, "ACTIVE");
    log.info(cartItems.toString());
        if (cartItems.isEmpty()) {
            throw new AppException(ErrorCode.CARTITEMS_IS_NOT_PRODUCT_IN_CART);
        }

        // Cập nhật trạng thái các item trong giỏ hàng
        for (CartItem cartItem : cartItems) {
            cartItem.setStatus(status); // Cập nhật trạng thái là "PAID"
        }

        cartItemRepository.saveAll(cartItems); // Lưu tất cả các item đã cập nhật
        return null;
    }
}
