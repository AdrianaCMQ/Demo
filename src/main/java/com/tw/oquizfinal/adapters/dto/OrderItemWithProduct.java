package com.tw.oquizfinal.adapters.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemWithProduct {
    private Long itemId;
    private Long productId;
    private String title;
    private BigDecimal price;
    private String category;
    private int quantity;

}
