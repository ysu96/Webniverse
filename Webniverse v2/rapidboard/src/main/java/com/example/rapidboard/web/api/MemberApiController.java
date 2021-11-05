package com.example.rapidboard.web.api;

import com.example.rapidboard.config.auth.PrincipalDetails;
import com.example.rapidboard.domain.member.Member;
import com.example.rapidboard.handler.exception.CustomException;
import com.example.rapidboard.handler.exception.CustomValidationException;
import com.example.rapidboard.service.AuthService;
import com.example.rapidboard.service.MemberService;
import com.example.rapidboard.web.dto.CmDto;
import com.example.rapidboard.web.dto.member.MemberUpdateDto;
import com.example.rapidboard.web.dto.member.SignupDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {
    private final MemberService memberService;
    private final AuthService authService;

    @PutMapping("/api/member/{memberId}")
    public ResponseEntity<?> updateMember(@PathVariable Long memberId,
                                          @Valid MemberUpdateDto memberUpdateDto,
                                          @AuthenticationPrincipal PrincipalDetails principalDetails) {

        log.info("[update member] member id : {}, memberUpdateDto : {}", memberId, memberUpdateDto.toString());
        if (!principalDetails.getMember().getMemberId().equals(memberId)) {
            throw new CustomValidationException("You have no authority to update user.");
        }

        Member member = memberService.updateMemberProfile(memberId, memberUpdateDto);
        principalDetails.setMember(member); //세션 수정
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/member/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable Long memberId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("[delete member] member id : {}", memberId);
        if (!Objects.equals(principalDetails.getMember().getMemberId(), memberId)) {
            throw new CustomException("You have no authority to delete user.");
        }

        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/duplicate/{username}")
    public ResponseEntity<?> checkDuplicateUsername(@PathVariable String username){
        log.info("username : {}", username);
        return new ResponseEntity<>(authService.checkDuplicateUsername(username),HttpStatus.OK);
    }
}
