package com.datn.service.impl;

import com.datn.entity.CartItem;
import com.datn.entity.User;
import com.datn.exception.AppException;
import com.datn.exception.ErrorCode;
import com.datn.repository.CartItemRepository;
import com.datn.repository.ProductRepository;
import com.datn.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");// co the kem them file html hoac design mail gui di
        mimeMessageHelper.setTo(to); // send toi dia chi mail nao
        mimeMessageHelper.setSubject(subject);// kem subject la gi
        mimeMessageHelper.setText(body, true);// set text cho phep thiet ke mail bang html
        mailSender.send(mimeMessage);
    }

    public User sendEmailProduct(String cartId) {
        // Tiêu đề email
        String subject = "Thông tin đơn hàng của bạn";

        // Lấy danh sách CartItem dựa vào cartId
        List<CartItem> cartItemList = cartItemRepository.findByCartIdAndStatus(cartId, "PAID");

        if (cartItemList.isEmpty()) {
            throw new AppException(ErrorCode.CART_NOT_EXITS);
        }

        // Lấy thông tin người dùng hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Tạo nội dung email động
        StringBuilder htmlMessage = new StringBuilder();
        htmlMessage.append("<html>")
                .append("<body style=\"font-family: Arial, sans-serif;\">")
                .append("<div style=\"background-color: #f5f5f5; padding: 20px;\">")
                .append("<h2 style=\"color: #333;\">Cảm ơn bạn đã mua hàng tại Tuan Book App!</h2>")
                .append("<p style=\"font-size: 16px;\">Dưới đây là danh sách sản phẩm trong đơn hàng của bạn:</p>")
                .append("<table style=\"width: 100%; border-collapse: collapse;\">")
                .append("<thead>")
                .append("<tr style=\"background-color: #007bff; color: #fff; text-align: left;\">")
                .append("<th style=\"padding: 10px; border: 1px solid #ddd;\">Tên sản phẩm</th>")
                .append("<th style=\"padding: 10px; border: 1px solid #ddd;\">Link tải</th>")
                .append("</tr>")
                .append("</thead>")
                .append("<tbody>");

        for (CartItem cartItem : cartItemList) {
            // .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND, "Sản phẩm không tồn tại."));
            htmlMessage.append("<tr>")
                    .append("<td style=\"padding: 10px; border: 1px solid #ddd;\">").append(cartItem.getProduct().getName()).append("</td>")
                    .append("<td style=\"padding: 10px; border: 1px solid #ddd;\">")
                    .append("<a href=\"").append(cartItem.getProduct().getLinkDrive()).append("\" style=\"color: #007bff;\">Tải xuống</a>")
                    .append("</td>")
                    .append("</tr>");
        }

        htmlMessage.append("</tbody>")
                .append("</table>")
                .append("<p style=\"font-size: 14px; margin-top: 20px;\">Nếu bạn có bất kỳ thắc mắc nào, vui lòng liên hệ với chúng tôi qua email này.</p>")
                .append("</div>")
                .append("</body>")
                .append("</html>");

        // Gửi email
        try {
            sendEmail(user.getEmail(), subject, htmlMessage.toString());
        } catch (MessagingException e) {
            // Xử lý lỗi khi gửi email
            throw new AppException(ErrorCode.EMAIL_SEND_FAILED);
        }
        return user;
    }

}
