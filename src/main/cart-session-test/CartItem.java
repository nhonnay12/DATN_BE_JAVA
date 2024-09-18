package com.datn.models.dto.request;

import com.datn.models.entity.Product;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private Product product;
    private int quantity;
}
