package com.example.rapidboard.web.dto.post;

import com.example.rapidboard.domain.board.Board;
import com.example.rapidboard.domain.member.Member;
import com.example.rapidboard.domain.post.Post;
import com.example.rapidboard.domain.postcontent.Postcontent;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class PostDto {
    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 100)
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    @Size(max = 65535)
    private String content;

    private Long board_id;

    List<MultipartFile> uploadFiles;
}
