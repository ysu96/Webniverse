package com.example.rapidboard.service;

import com.example.rapidboard.domain.post.Post;
import com.example.rapidboard.domain.comment.Comment;
import com.example.rapidboard.domain.comment.CommentRepository;
import com.example.rapidboard.domain.member.Member;
import com.example.rapidboard.handler.exception.CustomException;
import com.example.rapidboard.web.dto.comment.CommentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final PostService postService;

    @Transactional
    public void createComment(CommentDto commentDto, Long memberId){
        Post post = new Post();
        Member memberEntity = memberService.findById(memberId);
        Comment comment;

        //root comment
        if(commentDto.getCommentId() == null){
            post.setPostId(commentDto.getPostId());
            comment = Comment.createComment(post,memberEntity, commentDto.getContent(),null,0,0,0);
            Comment commentEntity = commentRepository.save(comment);

            int lastCommentGroup = commentRepository.getLastGroup(commentDto.getPostId());
            commentEntity.setCommentGroup(lastCommentGroup+1);
        }
        //child comment
        else {
            Comment parent = findById(commentDto.getCommentId());
            post.setPostId(parent.getPost().getPostId());
            int parentCommentOrder = parent.getCommentOrder();
            int parentCommentGroup = parent.getCommentGroup();
            //뒷순서 밀기
            commentRepository.reorder(parentCommentGroup, parentCommentOrder, post.getPostId());

            comment = Comment.createComment(post,memberEntity, commentDto.getContent(), parent, parent.getDepth()+1, parentCommentGroup, parentCommentOrder+1);
            commentRepository.save(comment);
        }
        Post postEntity = postService.findById(post.getPostId());
        postEntity.addCommentCount();
    }

    @Transactional
    public void deleteComment(Long commentId){
        Comment commentEntity = findById(commentId);
        Post postEntity = commentEntity.getPost();
        postEntity.reduceCommentCount();
        commentEntity.deleteComment();
    }

    @Transactional
    public void updateComment(Long commentId, String content){
        Comment commentEntity = findById(commentId);
        commentEntity.updateComment(content);
    }

    @Transactional
    public Comment findById(Long commentId){
        Comment commentEntity = commentRepository.findById(commentId).orElseThrow(()->{
            throw new CustomException("Comment does not exist.");
        });
        if (commentEntity.getIsDeleted() == 1) throw new CustomException("Comment does not exist.");
        return commentEntity;
    }

    @Transactional
    public Page<Comment> getComments(Post post, Pageable pageable) {
        return commentRepository.findAllByPostOrderByCommentGroupAscCommentOrderAsc(post, pageable);
    }
}
