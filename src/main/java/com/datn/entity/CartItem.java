package com.datn.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_cartitem")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonBackReference // Đánh dấu quan hệ "con"
    private Cart cart;
    @ManyToOne
    private Product product; // map vơi id của bảng product của bên product service
    //private String orderId;
    private int quantity;
    private long price;
    private String status;

}