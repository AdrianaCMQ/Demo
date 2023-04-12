package com.tw.oquizfinal.applications.exceptions;

import com.tw.oquizfinal.support.exceptions.BaseException;

public class ProductNotExistException extends BaseException {
    public ProductNotExistException(Long productId) {
        super("Product doesn't exist: " + productId);
    }
}
