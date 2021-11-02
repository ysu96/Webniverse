package com.example.rapidboard.web;

import com.example.rapidboard.config.auth.PrincipalDetails;
import com.example.rapidboard.domain.board.Board;
import com.example.rapidboard.domain.post.Post;
import com.example.rapidboard.domain.post.PostRepository;
import com.example.rapidboard.domain.uploadfile.Uploadfile;
import com.example.rapidboard.handler.exception.CustomException;
import com.example.rapidboard.service.BoardService;
import com.example.rapidboard.service.PageService;
import com.example.rapidboard.service.PostService;
import com.example.rapidboard.service.UploadfileService;
import com.example.rapidboard.web.dto.PagingDto;
import com.example.rapidboard.web.dto.post.PostShowDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    private final BoardService boardService;
    private final PageService pageService;
    private final UploadfileService uploadfileService;

    @GetMapping({"/", "/post/list"})
    public String postList(Model model, @PageableDefault(size = 5) Pageable pageable) {
        log.info("board list");
        Page<Post> posts = pageService.getAllPost(pageable);
        PagingDto pageDto = pageService.getPageInfo(posts);

        model.addAttribute("boardName", "All Posts");
        model.addAttribute("posts", posts);
        model.addAttribute("pageDto", pageDto);
        return "post/postList";
    }

    @GetMapping({"/post/{boardId}/list"})
    public String postBoardList(@PathVariable Long boardId, Model model, @PageableDefault(size = 5) Pageable pageable) {
        log.info("[board post list] board id : {}", boardId);
        Page<Post> posts = pageService.getBoardPost(boardId,pageable);
        PagingDto pageDto = pageService.getPageInfo(posts);
        String boardName = boardService.getBoardName(boardId);

        model.addAttribute("boardName", boardName);
        model.addAttribute("posts", posts);
        model.addAttribute("pageDto", pageDto);
        return "post/postList";
    }

    @GetMapping("/post/list/{postId}")
    public String postShow(@PathVariable Long postId, Model model, @PageableDefault(size = 10) Pageable pageable, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("[show post] post id : {}", postId);
        PostShowDto dto = pageService.showPost(postId, principalDetails, pageable);
        PagingDto pageDto = pageService.getPageInfo(dto.getComments());

        model.addAttribute("dto", dto);
        model.addAttribute("pageDto", pageDto);
        return "post/show";
    }

    @GetMapping("/post/post/{memberId}")
    public String postForm(@PathVariable Long memberId, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        log.info("[post form] member id : {}", memberId);
        if(memberId != principalDetails.getMember().getMemberId()){
            throw new CustomException("You have no authority to write.");
        }

        if(boardService.getBoardList().size() == 0){
            throw new CustomException("Create Board first.");
        }

        return "post/post";
    }

    @GetMapping("/post/update/{postId}")
    public String updateForm(@PathVariable Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        log.info("[post update form] post id : {}", postId);
        Post postEntity = postService.findById(postId);
        List<Uploadfile> uploadfiles = uploadfileService.getUploadfilesByPostAndIs_deleted(postEntity);
        //post의 주인이 아니고 관리자가 아닐경우 예외처리
        if(postEntity.getMember().getMemberId() != principalDetails.getMember().getMemberId() && !principalDetails.getMember().getRole().equals("ROLE_ADMIN")){
            throw new CustomException("You have no authority to update.");
        }

        model.addAttribute("post", postEntity);
        model.addAttribute("uploadfiles", uploadfiles);

        return "post/update";
    }

    @GetMapping("/post/search")
    public String searchPost(@RequestParam("searchType") String type, @RequestParam("keyword") String keyword, @PageableDefault(size = 10) Pageable pageable, Model model) {
        log.info("[post search] type : {}, keyword : {}", type, keyword);
        Page<Post> searchResult = pageService.searchPost(type, keyword, pageable);
        PagingDto pageDto = pageService.getPageInfo(searchResult);

        model.addAttribute("posts", searchResult);
        model.addAttribute("pageDto", pageDto);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

        if (!searchResult.hasContent()) {
            return "/post/nosearch";
        }
        return "post/search";

    }
}
