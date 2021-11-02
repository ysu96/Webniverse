package com.example.rapidboard.web;

import com.example.rapidboard.domain.member.Member;
import com.example.rapidboard.service.AuthService;
import com.example.rapidboard.web.dto.member.SignupDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@Slf4j
public class AuthController {
    private final AuthService authService;

    @GetMapping("/auth/signin")
    public String signinForm() {
        return "auth/signin";
    }

    @GetMapping("/auth/signup")
    public String signupForm() {
        return "auth/signup";
    }

    @PostMapping("/auth/signup")
    public String signup(SignupDto signupDto){
        log.info("[signup] signupDto : {}", signupDto.toString());
        authService.signup(signupDto);
        return "auth/signin";
    }
}
