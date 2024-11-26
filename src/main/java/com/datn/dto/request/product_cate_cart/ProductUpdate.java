package com.datn.dto.request.product_cate_cart;

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
    private int quantity = 1;
    private boolean status= true;
    private Long category_id;
    private Integer author_id;
    private Integer publisher_id;
    private String user_id;
    private String linkDrive;
}
