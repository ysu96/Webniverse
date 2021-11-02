package com.example.rapidboard.service;

import com.example.rapidboard.domain.member.Member;
import com.example.rapidboard.domain.member.MemberRepository;
import com.example.rapidboard.handler.exception.CustomException;
import com.example.rapidboard.web.dto.CmDto;
import com.example.rapidboard.web.dto.member.SignupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void signup(SignupDto signupDto){
        if(signupDto.getIsValidateCheck() == 0){
            throw new CustomException("You need to check for duplicate Username.");
        }

        String username = signupDto.getUsername();
        String name = signupDto.getName();
        String email = signupDto.getEmail();

        //유저네임은 영어 숫자만
        String usernamePattern = "^[A-Za-z0-9+]*$";
        if (!Pattern.matches(usernamePattern, username)) {
            throw new CustomException("Username must be in English and Number");
        }

        //이름은 한글 or 영어로
        String numberPattern = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]+$";
        if (!Pattern.matches(numberPattern, name)) {
            throw new CustomException("Name must be in Korean or English.");
        }

        //이메일
        String emailPattern = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
        if(!Pattern.matches(emailPattern, email)){
            throw new CustomException("Email format is incorrect.");
        }


        Member member = signupDto.toEntity();
        if(memberRepository.findByUsername(member.getUsername()).isPresent()){
            throw new CustomException("You need to check for duplicate Username.");
        }

        String rawPassword = member.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        member.setPassword(encPassword);
        if(member.getRole() == null) member.setRole("ROLE_USER"); //관리자 : ROLE_ADMIN

        memberRepository.save(member);
    }

    @Transactional
    public CmDto checkDuplicateUsername(String username){
        String pattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$";
        String usernamePattern = "^[A-Za-z0-9+]*$";

        if(username.length()<2 || username.length()>100){
            return new CmDto<>(0,"Username must be between 2 and 100 characters.", null);
        }
        else if(!Pattern.matches(pattern,username)){
            return new CmDto<>(0,"A space or special character is not allowed in Username.", null);
        }
        else if(!Pattern.matches(usernamePattern, username)){
            return new CmDto<>(0, "Username must be in English and Number", null);
        }
        else if(memberRepository.findByUsername(username).isPresent()){
            return new CmDto<>(0, "Duplicate Username.", null);
        }
        else{
            return new CmDto<>(1, "Available Username.", null);
        }
    }
}
