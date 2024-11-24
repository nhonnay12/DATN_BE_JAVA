package com.datn.models.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Key error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least 4 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD(1004, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    PERMISSION_EXISTED(1009, "Permission existed", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTED(1010, "Permission not existed", HttpStatus.NOT_FOUND),
    ROLE_NOT_EXISTED(1011, "Role not existed", HttpStatus.NOT_FOUND),
    PRICE_INVALID(1012, "Price must be number", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(1013, "Category not existed", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_EXISTED(1014, "Product not existed", HttpStatus.NOT_FOUND),
    PUBLISHER_NOT_EXISTED(1014, "Publisher not existed", HttpStatus.NOT_FOUND),
    AUTHOR_NOT_EXISTED(1014, "Author not existed", HttpStatus.NOT_FOUND),
    IMAGE_NOT_EXISTED(1015, "Image not existed", HttpStatus.BAD_REQUEST),
    FILE_NOT_SUPPORT(1016, "Image not support", HttpStatus.NOT_FOUND),
    FILE_NOT_UPLOAD(1017, "Image not upload", HttpStatus.NOT_FOUND),
    INVALID_FILE(1018, "Invalid file", HttpStatus.NOT_FOUND),
    DIRECTORY_CREATION_FAILED(1019, "DIRECTORY_CREATION_FAILED", HttpStatus.BAD_REQUEST),
    AUTHOR_EXISTED(1010, "Author existed", HttpStatus.BAD_REQUEST),
    PUBLISHER_EXISTED(1011, "Publisher existed", HttpStatus.BAD_REQUEST),
    PRO_NOT_EXIST_IN_CART(1012, "Product not exist in cart", HttpStatus.BAD_REQUEST),
    CART_EMPTY(1014, "Cart is empty", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_NULL(1015, "Email not null", HttpStatus.BAD_REQUEST),
    PHONE_NOT_NULL(1016, "Phone not null", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID_GMAIL_FORMAT(1017, "Email must be format @gmail.com", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_EXISTED(1018, "Email not existed", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1019, "Email existed", HttpStatus.BAD_REQUEST),
    CART_NOT_EXITS(1020, "CART_NOT_EXITS", HttpStatus.BAD_REQUEST),
    PRODUCT_EXISTED_TO_CART(1021, "Sản phẩm đã có trong giỏ hàng", HttpStatus.BAD_REQUEST),
    ORDER_NOT_EXIST(1022, "ORDER NOT EXIST", HttpStatus.BAD_REQUEST),
    USER_BANED(1023, "USER_BANED", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_CHANGES(1024, "PRODUCT_NOT_CHANGES", HttpStatus.BAD_REQUEST),
    PRODUCT_EXISTED(1025, "PRODUCT EXISTED", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_ACTIVE(1026, "PRODUCT_NOT_ACTIVE", HttpStatus.BAD_REQUEST),
    CARTITEMS_IS_NOT_PRODUCT_IN_CART(1027, "CARTITEMS_IS_NOT_PRODUCT_IN_CART", HttpStatus.BAD_REQUEST),
    EMAIL_SEND_FAILED(1028, "EMAIL_SEND_FAILED", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
