package com.example.rapidboard.web;

import com.example.rapidboard.config.auth.PrincipalDetails;
import com.example.rapidboard.domain.board.Board;
import com.example.rapidboard.handler.exception.CustomException;
import com.example.rapidboard.service.BoardService;
import com.example.rapidboard.service.PageService;
import com.example.rapidboard.service.PostService;
import com.example.rapidboard.service.MemberService;
import com.example.rapidboard.web.dto.PagingDto;
import com.example.rapidboard.web.dto.member.MemberProfileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final PostService postService;
    private final BoardService boardService;
    private final PageService pageService;

    @GetMapping("/member/{memberId}")
    public String profile(@PathVariable Long memberId, Model model, @PageableDefault(size = 10) Pageable pageable) {
        log.info("[member profile] member id : {}",memberId);
        MemberProfileDto dto = memberService.getUserProfile(memberId, pageable);
        PagingDto pageDto = pageService.getPageInfo(dto.getPosts());

        model.addAttribute("dto", dto);
        model.addAttribute("pageDto", pageDto);
        return "member/profile";
    }

    @GetMapping("/member/{memberId}/update")
    public String updateForm(@PathVariable Long memberId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("[member update form] member id : {}",memberId);
        //세션 주인과 수정페이지 유저가 다른 경우
        if(!Objects.equals(principalDetails.getMember().getMemberId(), memberId)){
            throw new CustomException("You have no authority to modify.");
        }

        return "member/update";
    }
}
