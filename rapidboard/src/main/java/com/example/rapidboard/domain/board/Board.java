package com.example.rapidboard.domain.board;

import com.example.rapidboard.domain.comment.Comment;
import com.example.rapidboard.domain.post.Post;
import com.example.rapidboard.domain.uploadfile.Uploadfile;
import com.example.rapidboard.handler.exception.CustomException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.List;
import java.util.regex.Pattern;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Slf4j
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;
    //test
    @JsonIgnore
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Post> posts;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, name = "is_deleted")
    private int isDeleted = 0;

    public static Board createBoard(String boardname){
        Board board = new Board();
        board.name = boardname;
        return board;
    }

    public void deleteBoard(){
        this.isDeleted = 1;
        List<Post> posts = this.getPosts();
        for(Post p : posts){
            p.deletePost();
            for(Comment c : p.getComments()){
                c.deleteComment();
            }
            for (Uploadfile f : p.getUploadfiles()) {
                f.deleteFile();
            }
        }
    }
}
