package com.example.rapidboard.web.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentDto {
    @NotBlank(message = "댓글을 입력하세요.")
    private String content;

    private Long postId;
    
    private Long commentId;

    private int depth;
}
