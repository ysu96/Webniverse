package com.example.rapidboard.domain.member;

import com.example.rapidboard.domain.post.Post;
import com.example.rapidboard.domain.comment.Comment;
import com.example.rapidboard.domain.uploadfile.Uploadfile;
import com.example.rapidboard.web.dto.member.MemberProfileDto;
import com.example.rapidboard.web.dto.member.MemberUpdateDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import javax.persistence.*;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 15)
    private String role;

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Post> posts;

    @Column(nullable = false, name = "is_deleted")
    private int isDeleted = 0;

    public MemberProfileDto getMemberProfileDto(Page<Post> posts){
        MemberProfileDto dto = new MemberProfileDto();
        dto.setMember(this);
        dto.setPosts(posts);
        dto.setPostCount((int)posts.getTotalElements());
        return dto;
    }

    public void updateMemberProfile(MemberUpdateDto memberUpdateDto, String encPassword){
        this.name = memberUpdateDto.getName();
        this.password = encPassword;
    }

    public void deleteMember(){
        this.isDeleted = 1;
        List<Post> posts = this.getPosts();
        // 사용자가 쓴 글 delete
        for(Post p : posts){
            p.deletePost();
            // 해당 글의 댓글 delete
            for(Comment c : p.getComments()){
                c.deleteComment();
            }
            for (Uploadfile f : p.getUploadfiles()) {
                f.deleteFile();
            }
        }

        //사용자가 쓴 댓글 delete
        List<Comment> comments = this.getComments();
        for (Comment c : comments) {
            c.deleteComment();
        }
    }
}
