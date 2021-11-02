package com.example.rapidboard.domain.comment;

import com.example.rapidboard.domain.post.Post;
import com.example.rapidboard.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;


    @JsonIgnore
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JsonIgnore
    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Column(nullable = false, length = 100, name = "commentContent")
    private String commentContent;

    @JsonIgnore
    @JoinColumn(name = "parent_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parent;

    @JsonIgnore
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Comment> children;

    @Column(name = "update_date")
    private String updateDate;

    @Column(nullable = false, name = "create_date")
    private String createDate;

    @Column(nullable = false)
    private int depth;

    @Column(nullable = false, name = "comment_group")
    private int commentGroup;

    @Column(nullable = false, name = "comment_order")
    private int commentOrder;

    @Column(nullable = false, name = "is_deleted")
    private int isDeleted = 0;

    @PrePersist // db에 insert 되기 직전에 실행
    public void createDate() {
        this.createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

    public static Comment createComment(Post post, Member member, String commentContent, Comment parent, int depth, int commentGroup, int commentOrder){
        return Comment.builder()
                .post(post)
                .member(member)
                .commentContent(commentContent)
                .parent(parent)
                .depth(depth)
                .commentGroup(commentGroup)
                .commentOrder(commentOrder)
                .build();
    }

    public void deleteComment(){
        this.isDeleted = 1;
    }

    public void updateComment(String content){
        this.commentContent = content;
        String updateDate =  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        this.updateDate = updateDate;
    }
}
