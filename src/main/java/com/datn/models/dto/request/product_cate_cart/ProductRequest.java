package com.datn.models.dto.request.product_cate_cart;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;
    private String description;
    private Double price;
    private int quantity = 1;
    private boolean status= true;
    private Long category_id;
    private int author_id;
    private int publisher_id;
}
