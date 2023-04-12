package com.tw.oquizfinal.adapter.dto.requests;

import com.tw.oquizfinal.domain.orderItem.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderRequest {
    @NotBlank(message = "addressee cannot be blank")
    @Size(min = 1, max = 50, message = "addressee should be from 1 to 50")
    private String addressee;
    @NotBlank(message = "address cannot be blank")
    @Size(min = 1, max = 255, message = "address should be from 1 to 255")
    private String address;
    @NotBlank(message = "mobile cannot be blank")
    @Pattern(regexp = "\\d+")
    @Size(min = 11, max = 11)
    private String mobile;
    @NotNull
    private Long couponId;
    @NotEmpty(message = "orderItems cannot be empty")
    private List<OrderItem> items;
}
