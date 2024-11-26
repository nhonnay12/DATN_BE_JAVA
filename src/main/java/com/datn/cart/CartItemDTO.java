package com.datn.cart;

import com.datn.entity.Product;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private String id;
    private Product product;
    private int quantity;
    private long price;
    private String status;
}