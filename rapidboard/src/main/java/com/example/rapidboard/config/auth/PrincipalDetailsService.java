package com.example.rapidboard.config.auth;

import com.example.rapidboard.domain.member.Member;
import com.example.rapidboard.domain.member.MemberRepository;
import com.example.rapidboard.handler.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername :: login username : {}",username);
        Member memberEntity = memberRepository.findByUsername(username).orElseThrow(()->{
            throw new InternalAuthenticationServiceException("Login fail");
        });

        if(memberEntity.getIsDeleted()==1){
            log.info("test");
            throw new DisabledException("Deleted");
        }

        return new PrincipalDetails(memberEntity);
    }
}
