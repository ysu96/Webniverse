package com.example.rapidboard.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagingDto {
    private int startIdx;
    private int endIdx;
    private int pageSize; // 한번에 보여주는 페이지 목록 개수 (5)
    private int curPage;
    private int totalPage;

    private int nextStartIdx;
    private int prevStartIdx;

}
