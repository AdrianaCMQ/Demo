package com.tw.oquizfinal.applications.exceptions;

import com.tw.oquizfinal.support.exceptions.BaseException;

public class OrderItemIsEmptyException extends BaseException {
    public OrderItemIsEmptyException(Long productId) {
        super("OrderItem doesn't exist for order: " + productId);
    }
}
