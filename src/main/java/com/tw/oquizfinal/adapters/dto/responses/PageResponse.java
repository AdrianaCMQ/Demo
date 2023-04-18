package com.tw.oquizfinal.adapters.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageResponse {
    private Integer current;
    private Integer size;
    private Integer total;

    public PageResponse(Integer current, Integer size, Integer total) {
        this.current = current;
        this.size = size;
        this.total = total;
    }
}
