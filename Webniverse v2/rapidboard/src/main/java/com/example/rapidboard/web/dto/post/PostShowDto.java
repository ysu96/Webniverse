package com.example.rapidboard.web.dto.post;

import com.example.rapidboard.domain.comment.Comment;
import com.example.rapidboard.domain.post.Post;
import com.example.rapidboard.domain.postcontent.Postcontent;
import com.example.rapidboard.domain.uploadfile.Uploadfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PostShowDto {
    private boolean pageOwnerState;
    private Post post;
    private Postcontent postcontent;
    private Page<Comment> comments;
    private String boardName;
    private List<Uploadfile> uploadfiles;
}
