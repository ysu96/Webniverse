package com.example.rapidboard.web.dto.member;

import com.example.rapidboard.domain.member.Member;
import com.example.rapidboard.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
public class MemberProfileDto {
    private int postCount;
    private Member member;
    private Page<Post> posts;
}
