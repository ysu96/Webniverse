package com.example.rapidboard.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmDto<T> {
    private int code; // 1(성공) , -1(실패)
    private String message;
    private T data;
}
