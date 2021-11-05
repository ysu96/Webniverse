package com.example.rapidboard.service;

import com.example.rapidboard.config.auth.PrincipalDetails;
import com.example.rapidboard.domain.comment.Comment;
import com.example.rapidboard.domain.post.Post;
import com.example.rapidboard.handler.exception.CustomException;
import com.example.rapidboard.web.dto.PagingDto;
import com.example.rapidboard.web.dto.post.PostShowDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PageService {
    private final PostService postService;
    private final CommentService commentService;
    private final MemberService memberService;
    private final UploadfileService uploadfileService;

    @Transactional
    public Page<Post> getAllPost(Pageable pageable) {
        return postService.getAllPosts(pageable);
    }

    @Transactional
    public Page<Post> getBoardPost(Long boardId, Pageable pageable) {
        return postService.getBoardPosts(boardId, pageable);
    }

    public PagingDto getPageInfo(Page<?> posts){
        int pageSize = 5;
        int totalPage = posts.getTotalPages();
        int curPage = posts.getPageable().getPageNumber();
        int startIdx = ((int)(curPage/pageSize) * pageSize);
        int endIdx = ((int)(curPage/pageSize) * pageSize) + pageSize -1;
        if(endIdx > totalPage-1) endIdx = totalPage-1;
        if(endIdx < 0) endIdx = 0;

        int nextStartIdx = startIdx + pageSize;
        if(posts.getTotalPages()-1 < nextStartIdx) nextStartIdx = posts.getTotalPages()-1;
        int prevStartIdx = startIdx - pageSize;
        if(prevStartIdx < 0) prevStartIdx = 0;

        return new PagingDto(startIdx, endIdx, pageSize, curPage, totalPage, nextStartIdx, prevStartIdx);
    }

    @Transactional
    public PostShowDto showPost(Long postId, PrincipalDetails principalDetails, Pageable pageable){
        PostShowDto dto = new PostShowDto();
        Post postEntity = postService.findById(postId);
        Page<Comment> comments = commentService.getComments(postEntity, pageable);

        dto.setPostcontent(postEntity.getPostcontent());
        dto.setComments(comments);
        dto.setPost(postEntity);
        dto.setBoardName(postEntity.getBoard().getName());

        dto.setUploadfiles(uploadfileService.getUploadfilesByPostAndIs_deleted(postEntity));
        log.info("{}",dto.getUploadfiles().size());

        if(principalDetails == null){
            dto.setPageOwnerState(false);
        }
        else{
            Long principalId = principalDetails.getMember().getMemberId();
            Long memberId = postEntity.getMember().getMemberId();
            String memberRole = memberService.findById(principalId).getRole();
            dto.setPageOwnerState((memberId.equals(principalId))||(memberRole.equals("ROLE_ADMIN")));
        }
        return dto;
    }

    @Transactional
    public Page<Post> searchPost(String type, String keyword, Pageable pageable){
        Page<Post> searchResult;
        switch(type){
            case "title": // 제목
                searchResult = postService.getSearchResultT(keyword, pageable);
                break;
            case "content": // 내용
                searchResult = postService.getSearchResultC(keyword, pageable);
                break;
            case "writer": // 작성자
                searchResult = postService.getSearchResultM(keyword,pageable);
                break;
            case "titleContent":
                searchResult = postService.getSearchResultTC(keyword, pageable);
                break;
            default:
                throw new CustomException("search type error");
        }
        log.info("search debug");
        return searchResult;
    }
}
