package com.example.rapidboard.domain.uploadfile;

import com.example.rapidboard.domain.member.Member;
import com.example.rapidboard.domain.post.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Uploadfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uploadfile_id")
    private Long uploadfileId;

    @Column(nullable = false, name = "original_filename")
    private String originalFilename;

    @Column(nullable = false, unique = true, name = "server_filename")
    private String serverFilename;

    @Column(nullable = false, name = "server_filepath")
    private String serverFilepath;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false, name = "is_deleted")
    private int isDeleted = 0;

    public void deleteFile(){
        this.isDeleted = 1;
    }

}
