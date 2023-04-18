package com.tw.oquizfinal.adapters.mapper;

import com.tw.oquizfinal.adapters.dto.OrderItemWithProduct;
import com.tw.oquizfinal.adapters.dto.request.OrderRequest;
import com.tw.oquizfinal.adapters.dto.responses.OrderResponse;
import com.tw.oquizfinal.adapters.dto.responses.PageResponse;
import com.tw.oquizfinal.domain.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderDtoMapper {
    OrderDtoMapper MAPPER = Mappers.getMapper(OrderDtoMapper.class);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "items", source = "orderRequest.items")
    Order toModel(OrderRequest orderRequest, Instant createdAt);

    @Mapping(target = "orderItems", source = "itemWithProductList")
    OrderResponse toResponse(Order order, List<OrderItemWithProduct> itemWithProductList);

    default PageRequest buildPageRequest(Integer page, Integer size, String orderBy, String sortBy) {
        Sort.Direction direction = "desc".equals(orderBy) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(page - 1, size, direction, sortBy);
    }

    @Named("intPlusOne")
    default Integer intPlusOne(Integer result) {
        return result + 1;
    }

    @Mappings({
            @Mapping(target = "current", source = "number", qualifiedByName = "intPlusOne"),
            @Mapping(target = "total", source = "totalPages")
    })
    PageResponse toPageResponse(Page<Order> orders);

}

