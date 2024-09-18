package com.datn.controller.product;

import com.datn.models.dto.request.CartItem;
import com.datn.models.dto.response.ApiResponse;
import com.datn.models.dto.response.ProductResponse;
import com.datn.models.entity.Product;
import com.datn.service.Cart;
import com.datn.service.ProductService;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@RestController
@RequestMapping("/cart")
public class CartController {

    private final ProductService productService;

    public CartController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add/{productId}")
    public ApiResponse<List<CartItem>> addToCart(@PathVariable Long productId, HttpSession session) {
        // Lấy giỏ hàng từ session, nếu chưa có thì khởi tạo mới
        Product product = getProductById(productId); // Giả sử bạn đã có một ProductService để lấy thông tin sản phẩm
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();// tạo ra giỏ hàng mới
            cart.addCartItem(new CartItem(product, 1));
            session.setAttribute("cart", cart);// thêm giỏ hàng vào session
        }else{
            boolean existProduct = false;
            List<CartItem> cartItemList = cart.getCartItemList();
            for (CartItem cartItem : cartItemList) {
                if (cartItem.getProduct().getId().equals(productId)) {
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                    existProduct = true;
                    break;
                }
            }
            if (!existProduct) {
                cartItemList.add(new CartItem(product, 1));
            }
        }

        // Giả lập lấy thông tin sản phẩm từ ProductService
        // Thêm sản phẩm vào giỏ hàng

        return ApiResponse.<List<CartItem>>builder()
                .code(200)
                .message("Success")
                .result(cart.getCartItemList())
                .build();
    }

    @GetMapping("/items")
    public List<CartItem> getCartItems(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            return List.of(); // Trả về danh sách rỗng nếu giỏ hàng chưa được tạo
        }

        // Trả về danh sách sản phẩm trong giỏ hàng dưới dạng CartResponse
        return cart.getCartItemList().stream()
                .map(item -> CartItem.builder()
                        .quantity(item.getQuantity())// Giả lập ID giỏ hàng
                        .product(item.getProduct())
                        .build())
                .collect(Collectors.toList());
    }

    @DeleteMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.removeCartItem(productId);
        }
        return "Sản phẩm đã được xóa khỏi giỏ hàng!";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.clearCartItemList();
        }
        return "Giỏ hàng đã được làm trống!";
    }

    private Product getProductById(Long productId) {
        ProductResponse productResponse = productService.getProduct(productId);
        Product product = Product.builder()
                .id(productId)
                .name(productResponse.getName())
                .price(productResponse.getPrice())
                .category(productResponse.getCategory())
                .description(productResponse.getDescription())
                .status(productResponse.isStatus())
                .images(productResponse.getImages())
                .quantity(productResponse.getQuantity())
                .publisher(productResponse.getPublisher())
                .authors(productResponse.getAuthors())
                .build();
        return product;
    }
}
