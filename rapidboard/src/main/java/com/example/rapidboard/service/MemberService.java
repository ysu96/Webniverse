package com.example.rapidboard.service;


import com.example.rapidboard.domain.member.Member;
import com.example.rapidboard.domain.post.Post;
import com.example.rapidboard.domain.member.MemberRepository;
import com.example.rapidboard.handler.exception.CustomException;
import com.example.rapidboard.handler.exception.CustomValidationException;
import com.example.rapidboard.web.dto.CmDto;
import com.example.rapidboard.web.dto.member.MemberProfileDto;
import com.example.rapidboard.web.dto.member.MemberUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final PostService postService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public MemberProfileDto getUserProfile(Long memberId, Pageable pageable){
        Member memberEntity = findById(memberId);
        Page<Post> posts = postService.getMemberPosts(memberEntity, pageable);
        MemberProfileDto dto = memberEntity.getMemberProfileDto(posts);
        return dto;
    }

    @Transactional
    public Member updateMemberProfile(Long memberId, MemberUpdateDto memberUpdateDto){
        //현재 비밀번호 확인
        if(!bCryptPasswordEncoder.matches(memberUpdateDto.getCurrentPassword(), findById(memberId).getPassword())){
            throw new CustomValidationException("Current password is not correct.");
        }

        //이름은 한글 or 영어로
        String name = memberUpdateDto.getName();
        String numberPattern = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]+$";
        if (!Pattern.matches(numberPattern, name)) {
            throw new CustomValidationException("Name must be in Korean or English.");
        }

        String password;
        if (memberUpdateDto.getNewPassword().isBlank()) {
            log.info("no new password");
            password = memberUpdateDto.getCurrentPassword();
        }else{
            log.info("new password");
            password = memberUpdateDto.getNewPassword();
        }

        Member memberEntity = findById(memberId);
        String encPassword = bCryptPasswordEncoder.encode(password);
        memberEntity.updateMemberProfile(memberUpdateDto, encPassword);
        return memberEntity;
    }

    @Transactional
    public void deleteMember(Long memberId){
        Member memberEntity = findById(memberId);
        memberEntity.deleteMember();
    }

    @Transactional
    public Member findById(Long memberId){
        Member memberEntity = memberRepository.findById(memberId).orElseThrow(()->{
            throw new CustomException("Member does not exist.");
        });
        if (memberEntity.getIsDeleted() == 1) throw new CustomException("Member does not exist.");
        return memberEntity;
    }

    @Transactional
    public List<Member> findAllMember(){
        return memberRepository.findAllByIsDeleted(0);
    }

    @Transactional
    public List<Member> findRestMember(List<Member> participants) {
        List<Member> members = memberRepository.findAllByIsDeleted(0);
        for (Member m : participants) {
            members.remove(m);
        }
        return members;
    }
}
