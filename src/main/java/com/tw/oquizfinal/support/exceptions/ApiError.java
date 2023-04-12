package com.tw.oquizfinal.support.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApiError {
    Integer code;
    String error;
    String message;
}
