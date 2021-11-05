package com.example.rapidboard.web;

import com.example.rapidboard.config.auth.PrincipalDetails;
import com.example.rapidboard.domain.member.Member;
import com.example.rapidboard.domain.participate.Participate;
import com.example.rapidboard.domain.post.Post;
import com.example.rapidboard.domain.webinar.Webinar;
import com.example.rapidboard.service.MemberService;
import com.example.rapidboard.service.PageService;
import com.example.rapidboard.service.WebinarService;
import com.example.rapidboard.web.dto.PagingDto;
import com.example.rapidboard.web.dto.webinar.ParticipantLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.rule.Mode;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Set;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WebinarController {
    private final WebinarService webinarService;
    private final PageService pageService;
    private final MemberService memberService;

    @GetMapping("/webinar/create")
    public String createForm() {
        log.info("webinar create form");
        return "webinar/roomCreateForm";
    }

    @GetMapping("/webinar/list")
    public String roomList(Model model, @PageableDefault(size = 5) Pageable pageable) throws java.text.ParseException {
        log.info("webinar room list");
        Page<Webinar> rooms = webinarService.getRoomList(pageable);
        PagingDto pageDto = pageService.getPageInfo(rooms);

        model.addAttribute("rooms", rooms);
        model.addAttribute("pageDto", pageDto);
        return "webinar/roomList";
    }

    @GetMapping("/webinar/enter/{webinarId}")
    public String enterWebinar(@PathVariable Long webinarId, @AuthenticationPrincipal PrincipalDetails principalDetails) throws JsonProcessingException {
        String otp = webinarService.enterWebinar(webinarId, principalDetails.getMember().getMemberId(), principalDetails.getMember().getUsername());
        return "redirect:https://biz-dev.gooroomee.com/room/otp/" + otp;
    }
//
//    @GetMapping("/webinar/enter/{webinarId}")
//    public void enterWebinarTest(@PathVariable Long webinarId, @AuthenticationPrincipal PrincipalDetails principalDetails) throws ParseException {
//        webinarService.enterWebinarTest(webinarId, principalDetails.getMember().getMemberId(), principalDetails.getMember().getUsername());
////        return "redirect:https://biz-dev.gooroomee.com/room/otp/" + otp;
//    }
}
