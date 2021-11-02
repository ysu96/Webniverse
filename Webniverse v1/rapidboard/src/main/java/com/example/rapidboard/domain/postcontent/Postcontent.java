package com.example.rapidboard.domain.postcontent;

import com.example.rapidboard.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
public class Postcontent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postcontent_id")
    private Long postcontentId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String postcontent;

    public static Postcontent createPostcontent(String content){
        return Postcontent.builder()
                .postcontent(content)
                .build();
    }
}
