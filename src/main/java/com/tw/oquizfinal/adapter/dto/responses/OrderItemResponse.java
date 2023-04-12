package com.tw.oquizfinal.adapter.dto.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private int total;
    private String sortBy;
    private String orderBy;
    private List<OrderResponse> data;

    public static OrderItemResponse buildBy(List<OrderResponse> orderResponses, String sortBy, String orderBy) {
        OrderItemResponse orderItemResponse = new OrderItemResponse();
        orderItemResponse.setTotal(orderResponses.size());
        orderItemResponse.setSortBy(sortBy);
        orderItemResponse.setOrderBy(orderBy);
        orderItemResponse.setData(orderResponses);
        return orderItemResponse;
    }
}