package com.datn.cart;

import com.datn.models.entity.OrderStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {

    private String firstname; // bắt buộc

    private String lastname;

    private String country; // bắt buộc

    private String address;// địa chỉ người nhận bắt buộc

    private String town;

    private String state;// tên khu vực


    private String email; // bắt buộc

    private String phone; // băt buộc

    private String note;// chú ý

   // private long totalPrice;

    private String paymentmethod; // phương thức thanh toán

    private OrderStatus status;
}
