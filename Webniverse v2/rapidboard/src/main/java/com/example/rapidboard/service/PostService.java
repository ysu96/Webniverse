package com.example.rapidboard.service;

import com.example.rapidboard.config.auth.PrincipalDetails;
import com.example.rapidboard.domain.board.Board;
import com.example.rapidboard.domain.board.BoardRepository;
import com.example.rapidboard.domain.comment.Comment;
import com.example.rapidboard.domain.comment.CommentRepository;
import com.example.rapidboard.domain.post.Post;
import com.example.rapidboard.domain.post.PostRepository;
import com.example.rapidboard.domain.member.Member;
import com.example.rapidboard.domain.member.MemberRepository;
import com.example.rapidboard.domain.postcontent.Postcontent;
import com.example.rapidboard.domain.postcontent.PostcontentRepository;
import com.example.rapidboard.handler.exception.CustomException;
import com.example.rapidboard.web.dto.post.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final BoardService boardService;

    private final PostcontentRepository postcontentRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void addViewCount(Long postId){
        Post postEntity = findById(postId);
        postEntity.addViewCount();
    }

    @Transactional
    public Post createPost(Long memberId, PostDto postDto) {
        Member memberEntity = memberRepository.findById(memberId).orElseThrow(()->{
            throw new CustomException("Member does not exist.");
        });
        Board boardEntity = boardService.findById(postDto.getBoard_id());
        Postcontent postcontent = Postcontent.createPostcontent(postDto.getContent());
        postcontentRepository.save(postcontent);

        Post post = Post.createPost(memberEntity, boardEntity, postcontent, postDto.getTitle());
        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Long postId, PostDto postDto) {
        Post postEntity = findById(postId);
        postEntity.updatePost(postDto);
        return postEntity;
    }

    @Transactional
    public void deletePost(Long postId){
        Post postEntity = findById(postId);
        postEntity.deletePost();
    }

    @Transactional
    public Post findById(Long postId){
        Post postEntity = postRepository.findById(postId).orElseThrow(()->{
            throw new CustomException("Post does not exist.");
        });
        if (postEntity.getIsDeleted() == 1) throw new CustomException("Post does not exist.");
        return postEntity;
    }

    @Transactional
    public Page<Post> getMemberPosts(Member member, Pageable pageable) {
        return postRepository.findAllByMemberAndIsDeletedOrderByPostIdDesc(member, 0, pageable);
    }

    @Transactional
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAllByIsDeletedOrderByPostIdDesc(0, pageable);
    }

    @Transactional
    public Page<Post> getBoardPosts(Long boardId, Pageable pageable) {
        Board board = boardService.findById(boardId);
        return postRepository.findAllByBoardAndIsDeletedOrderByPostIdDesc(board, 0, pageable);
    }

    @Transactional
    public Page<Post> getSearchResultT(String keyword, Pageable pageable) {
        return postRepository.getSearchResultT(keyword, pageable);
    }

    @Transactional
    public Page<Post> getSearchResultC(String keyword, Pageable pageable) {
        return postRepository.getSearchResultC(keyword, pageable);
    }

    @Transactional
    public Page<Post> getSearchResultM(String keyword, Pageable pageable) {
        return postRepository.getSearchResultM(keyword, pageable);
    }
    @Transactional
    public Page<Post> getSearchResultTC(String keyword, Pageable pageable) {
        return postRepository.getSearchResultTC(keyword, pageable);
    }
}
