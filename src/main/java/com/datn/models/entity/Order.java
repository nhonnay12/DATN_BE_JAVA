

package com.datn.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "tbl_oder")
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;

    private String firstname; // bắt buộc

    private String lastname;

    private String country; // bắt buộc

    private String address;// địa chỉ người nhận bắt buộc

    private String town;

    private String state;// tên khu vực

   // private long postCode;// mã bưu chính

    private String email; // bắt buộc

    private String phone; // băt buộc

    private String note;// chú ý

    private long totalPrice;

    private String paymentmethod; // phương thức thanh toán

    private OrderStatus status;

    @OneToOne // Thay đổi thành @ManyToOne vì mỗi Order chỉ thuộc về một Cart
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;  // Quan hệ N-1 với Cart (mỗi Order có một Cart)

    @CreatedDate  // Đánh dấu trường này để tự động gán thời gian tạo
    private LocalDateTime createdAt;  // Trường thời gian tạo
private  String transactionId;
private String vnp_TxnRef;
private LocalDateTime paymentDate;
}