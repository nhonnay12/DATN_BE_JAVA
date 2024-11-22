package com.datn.models.entity;

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
