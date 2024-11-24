package com.datn.payment.vnpay;

import com.datn.cart.OrderService;
import com.datn.models.dto.response.ApiResponse;
import com.datn.models.entity.OrderStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/vn-pay")
    public ApiResponse<PaymentDTO.VNPayResponse> pay(HttpServletRequest request) {
//        return ApiResponse.<PaymentDTO.VNPayResponse>builder()
//                .code(200)
//                .message("Sucess")
//                .result(paymentService.createVnPayPayment(request))
//                .build();
        return new ApiResponse<>(200, "Success", paymentService.createVnPayPayment(request));
    }

    @GetMapping("/vn-pay-callback")
    public ApiResponse<Void> payCallbackHandler(HttpServletResponse response, @RequestParam Map<String, String> params) throws IOException {
        String vnp_ResponseCode = params.get("vnp_ResponseCode");  // Lấy tham số từ callback

        String vnp_TxnRef = params.get("vnp_TxnRef");
        String vnp_TransactionNo = params.get("vnp_TransactionNo");
        // Nếu không có vnp_ResponseCode thì trả về lỗi
//        if (vnp_ResponseCode == null) {
//            log.error("vnp_ResponseCode is missing!");
//            return new ApiResponse<>(400, "vnp_ResponseCode is missing", null);
//        }

        response.sendRedirect("http://localhost:3000/payment-callback?vnp_ResponseCode=" + vnp_ResponseCode+"&"  + "vnp_TxnRef=" + vnp_TxnRef +"&" + "vnp_TransactionNo=" + vnp_TransactionNo);
        // Xử lý logic tiếp theo dựa vào vnp_ResponseCode
        if (vnp_ResponseCode.equals("00")) {

           // log.info("Thanh toán thành công: " + vnp_ResponseCode);
            return ApiResponse.<Void>builder()
                    .code(200)
                    .message("Thanh toan thanh cong")
                    .build();

            //, new PaymentDTO.VNPayResponse("00", "Success", "")
        } else {
            log.info("Mã lỗi: " + vnp_ResponseCode);
            return new ApiResponse<>(400, "Thanh toán thất bại", null);
        }

        // Điều hướng với vnp_ResponseCode chính xác
    }


}
