package com.example.rapidboard.web.api;

import com.example.rapidboard.config.auth.PrincipalDetails;
import com.example.rapidboard.domain.comment.Comment;
import com.example.rapidboard.service.CommentService;
import com.example.rapidboard.service.PageService;
import com.example.rapidboard.web.dto.CmDto;
import com.example.rapidboard.web.dto.PagingDto;
import com.example.rapidboard.web.dto.comment.CommentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentApiController {
    private final CommentService commentService;
    private final PageService pageService;

    @PostMapping("/api/comment") //Json데이터를 받기 위해선 @RequestBody 써야함
    public ResponseEntity<?> createComment(@Valid @RequestBody CommentDto commentDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("[create comment] Comment dto : {}", commentDto.toString());
        commentService.createComment(commentDto, principalDetails.getMember().getMemberId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        log.info("[delete comment] comment id : {}", commentId);
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/comment")
    public ResponseEntity<?> updateComment(@Valid @RequestBody CommentDto commentDto, BindingResult bindingResult) {
        log.info("[update comment] Comment dto : {}", commentDto.toString());
        commentService.updateComment(commentDto.getCommentId(), commentDto.getContent());
        return ResponseEntity.noContent().build();
    }

}
