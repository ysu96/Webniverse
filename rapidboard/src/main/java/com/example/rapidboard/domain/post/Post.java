package com.example.rapidboard.domain.post;

import com.example.rapidboard.domain.board.Board;
import com.example.rapidboard.domain.comment.Comment;
import com.example.rapidboard.domain.member.Member;
import com.example.rapidboard.domain.postcontent.Postcontent;
import com.example.rapidboard.domain.uploadfile.Uploadfile;
import com.example.rapidboard.web.dto.post.PostDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(nullable = false, length = 100)
    private String title;

    @JsonIgnore
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "board_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Uploadfile> uploadfiles;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "postcontent_id", nullable = false)
    private Postcontent postcontent;

    @Column(nullable = false, name = "view_count")
    private int viewCount = 0;

    @Column(nullable = false, name = "comment_count")
    private int commentCount = 0;

    @Column(name = "update_date")
    private String updateDate;

    @Column(nullable = false, name = "create_date")
    private String createDate;

    @Column(nullable = false, name = "is_deleted")
    private int isDeleted = 0;

    @PrePersist // db에 insert 되기 직전에 실행
    public void createDate() {
        this.createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

    public static Post createPost(Member member, Board board, Postcontent postcontent, String title){
        return Post.builder()
                .postcontent(postcontent)
                .title(title)
                .member(member)
                .board(board)
                .build();
    }

    public void updatePost(PostDto postDto){
        this.getPostcontent().setPostcontent(postDto.getContent()); // 글 수정
        this.title = postDto.getTitle();
        String updateDate =  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        this.updateDate = updateDate;
    }

    public void deletePost(){
        this.isDeleted = 1;
        List<Comment> comments = this.getComments();
        for (Comment c : comments) {
            c.deleteComment();
        }
        List<Uploadfile> uploadfiles = this.getUploadfiles();
        for (Uploadfile uf : uploadfiles) {
            uf.deleteFile();
        }
    }

    public void addViewCount(){
        this.viewCount+=1;
    }

    public void addCommentCount(){
        this.commentCount+=1;
    }

    public void reduceCommentCount(){
        this.commentCount-=1;
    }
}
