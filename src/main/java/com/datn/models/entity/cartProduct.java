//package com.datn.models.entity;
//
//
//import jakarta.persistence.*;
//
//@Entity
//public class cartProduct {
//
//    @EmbeddedId
//    private cartProduct id;
//
//    @ManyToOne
//    @MapsId("cartId")
//    @JoinColumn(name = "cart_id")
//    private Cart cart;
//
//    @ManyToOne
//    @MapsId("productId")
//    @JoinColumn(name = "product_id")
//    private Product product;
//
//    // Các phương thức khác nếu cần
//}
