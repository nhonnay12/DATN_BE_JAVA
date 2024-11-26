package com.datn.dto.request.product_cate_cart;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    private String name;
    private String description;
}

