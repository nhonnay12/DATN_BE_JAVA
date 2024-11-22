package com.datn.models.dto;

import com.datn.models.entity.Cart;
import com.datn.models.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemDto {
    List<Product> product;
    int quantity;
   // public CartItemDto(Cart cart) {
        //this.quantity = cart.getQuantity();
        //this.product = cart.getProduct();
   // }
}
