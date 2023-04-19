package com.tw.oquizfinal.adapters.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderTotalResponse {
    private int total;
    private String sortBy;
    private String orderBy;
    private List<OrderResponse> data;

    public static OrderTotalResponse buildBy(List<OrderResponse> orderResponses) {

        OrderTotalResponse orderTotalResponse = new OrderTotalResponse();
        orderTotalResponse.setTotal(orderResponses.size());
        orderTotalResponse.setSortBy("createdAt");
        orderTotalResponse.setOrderBy("desc");
        orderTotalResponse.setData(orderResponses);
        return orderTotalResponse;
    }

    public static OrderTotalResponse buildBy(List<OrderResponse> orderResponses, String sortBy, String orderBy) {
        OrderTotalResponse orderTotalResponse = new OrderTotalResponse();
        orderTotalResponse.setTotal(orderResponses.size());
        orderTotalResponse.setSortBy(sortBy);
        orderTotalResponse.setOrderBy(orderBy);
        orderTotalResponse.setData(orderResponses);
        return orderTotalResponse;
    }
}
