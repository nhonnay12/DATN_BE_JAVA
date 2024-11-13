package com.datn.payment.vnpay;

import com.datn.models.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ApiResponse<PaymentDTO.VNPayResponse> payCallbackHandler(HttpServletResponse response, @RequestParam String vnp_ResponseCode) {
        //  String status = response.getParameter("vnp_ResponseCode");
        if ( vnp_ResponseCode.equals("00")) {
            log.info("MÃ LỖI" + vnp_ResponseCode);
            return new ApiResponse<>(200, "Success", new PaymentDTO.VNPayResponse("00", "Success", ""));
        } else {
            log.info("MÃ LỖI" + vnp_ResponseCode);
            return new ApiResponse<>(400, "Failed", null);
        }
    }
}
