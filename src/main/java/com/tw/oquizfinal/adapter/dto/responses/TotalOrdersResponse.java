package com.tw.oquizfinal.adapter.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TotalOrdersResponse {
    private PageResponse page;
    private OrderItemResponse items;

}
