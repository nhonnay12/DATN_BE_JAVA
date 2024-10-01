package com.datn.service.impl;

import com.datn.models.dto.AddToCart;
import com.datn.models.dto.CartDto;
import com.datn.models.dto.CartItemDto;
import com.datn.models.entity.Cart;
import com.datn.models.entity.Product;
import com.datn.models.entity.User;
import com.datn.models.exception.AppException;
import com.datn.models.exception.ErrorCode;
import com.datn.repository.CartRepository;
import com.datn.repository.ProductRepository;
import com.datn.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartRepository cartRepository;
    @Override
    public Cart addToCart(AddToCart addToCartDto, User user) {

        // validate if the product id is valid
        Product product = productRepository.findById(addToCartDto.getProduct_id()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));


        // find if the product is already in the user's cart
        Cart existProduct = cartRepository.findByUserAndProduct(user, product).orElse(null);
       // Cart existProduct = cartRepository.findByProduct(product).orElse(null);
        if (existProduct != null) {
            // If the product is already in the cart, update the quantity
            existProduct.setQuantity(existProduct.getQuantity() + addToCartDto.getQuantity());
            existProduct.setCreatedDate(new Date()); // update create date if needed
            return cartRepository.save(existProduct);
        } else {
            // If the product is not in the cart, create a new cart entry
            Cart newCart = new Cart();
            newCart.setProduct(product);
            newCart.setUser(user);
            newCart.setQuantity(addToCartDto.getQuantity());
            newCart.setCreatedDate(new Date());
            return cartRepository.save(newCart);
        }
    }
    @Override
    public CartDto listCartItems(User user) {
        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);

        List<CartItemDto> cartItems = new ArrayList<>();
        double totalCost = 0;
        for (Cart cart : cartList) {
            CartItemDto cartItemDto = new CartItemDto(cart);
            totalCost += cartItemDto.getQuantity() * cart.getProduct().getPrice();
            cartItems.add(cartItemDto);
        }

        CartDto cartDto = new CartDto();
        cartDto.setTotalPrice(totalCost);
        cartDto.setCartItems(cartItems);
        return cartDto;
    }
    @Override
    public void deleteCartItem(Integer cartItemId, User user) {
        // the item id belongs to user

        Cart optionalCart = cartRepository.findById(cartItemId).orElseThrow(() -> new AppException(ErrorCode.PRO_NOT_EXIST_IN_CART));
        cartRepository.delete(optionalCart);
    }
    @Override
    public void deleteAll(User user) {
        List<Cart> cart = cartRepository.findAllByUser(user);
        cartRepository.deleteAll(cart);
    }
}
