package com.datn.models.dto.request.product_cate_cart;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdate {
    private Long id;
    private String name;
    private String description;
    private Double price;
}
