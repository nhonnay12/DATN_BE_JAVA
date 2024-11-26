package com.datn.entity;

public enum OrderStatus {
    PENDING("PENDING"),
    CANCELLED("CANCELLED"),
    COMPLETED("COMPLETED");
    ;

    OrderStatus(String message) {
        this.message = message;
    }

    private String message;
}
