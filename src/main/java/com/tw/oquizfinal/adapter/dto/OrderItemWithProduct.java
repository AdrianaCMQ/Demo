package com.tw.oquizfinal.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderItemWithProduct {
    private Long itemId;
    private Long productId;
    private String title;
    private BigDecimal price;
    private String category;
    private int quantity;

}
