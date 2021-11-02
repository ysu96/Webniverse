package com.example.rapidboard.web.api;

import com.example.rapidboard.domain.board.Board;
import com.example.rapidboard.domain.post.Post;
import com.example.rapidboard.domain.uploadfile.Uploadfile;
import com.example.rapidboard.service.PostService;
import com.example.rapidboard.service.UploadfileService;
import com.example.rapidboard.web.dto.CmDto;
import com.example.rapidboard.web.dto.post.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostApiController {
    private final PostService postService;
    private final UploadfileService uploadfileService;

    @PostMapping("/api/post/{memberId}")
    public ResponseEntity<?> createPost(@PathVariable Long memberId,
                                        @ModelAttribute @Valid PostDto postDto
                                       ) throws IOException {
//Json이나 XML과 같은 형태의 데이터를 MessageConverter를 통해 변환시키는 @RequestBody와 달리,
//@ModelAttribute는 multipart/form-data 형태의 HTTP Body와 HTTP 파라미터들을 매핑시킨다.

        log.info("[create post] member id : {}", memberId);
        Post post = postService.createPost(memberId, postDto);
        if(postDto.getUploadFiles() != null){
            uploadfileService.saveFile(post, postDto.getUploadFiles());
        }

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/api/post/{postId}")
    public ResponseEntity<?> addPostViewCount(@PathVariable Long postId) {
        log.info("[add view count post] post id : {}", postId);
        postService.addViewCount(postId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/post/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        log.info("[delete post] post id : {}", postId);
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/post/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @ModelAttribute @Valid PostDto postDto) throws IOException {
        log.info("[update post] post id : {}, postDto : {}", postId, postDto.toString());
        Post post = postService.updatePost(postId, postDto);
        if(postDto.getUploadFiles() != null){
            uploadfileService.saveFile(post, postDto.getUploadFiles());
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download/{uploadfileId}")
    public ResponseEntity<Resource> fileDownload(@PathVariable Long uploadfileId) throws IOException {
        return uploadfileService.fileDownload(uploadfileId);
    }

    @DeleteMapping("/api/file/{uploadfileId}")
    public ResponseEntity<?> deleteFile(@PathVariable Long uploadfileId){
        uploadfileService.deleteFile(uploadfileId);
        return ResponseEntity.noContent().build();
    }
}
