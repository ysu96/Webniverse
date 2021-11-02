package com.example.rapidboard.config.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@Slf4j
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    private final String DEFAULT_FAILURE_URL = "/WEB-INF/views/auth/signinError.jsp";

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.info("login fail exception log : {}",e.toString());
        if (e instanceof InternalAuthenticationServiceException) {
            if(e.getMessage().equals("Deleted")){
                httpServletRequest.setAttribute("loginFailMsg", "이미 탈퇴한 회원입니다.");
            } else{
                httpServletRequest.setAttribute("loginFailMsg", "존재하지 않는 회원입니다.");
            }
        } else if(e instanceof BadCredentialsException) {
            httpServletRequest.setAttribute("loginFailMsg", "비밀번호가 일치하지 않습니다.");
        }

        // 로그인 페이지로 다시 포워딩
        RequestDispatcher dispatcher = httpServletRequest.getRequestDispatcher(DEFAULT_FAILURE_URL);
        dispatcher.forward(httpServletRequest, httpServletResponse);
    }
}
