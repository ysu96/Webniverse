package com.example.rapidboard.config;

import com.example.rapidboard.config.auth.CustomLoginFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@EnableWebSecurity // 해당 파일로 시큐리티를 활성화
@RequiredArgsConstructor
@Configuration //IoC
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomLoginFailureHandler customLoginFailureHandler;
    @Bean
    public BCryptPasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    } 

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); //csrf토큰 비활성화
        http.cors().disable();
        http.authorizeRequests()
                    .antMatchers("/member/**", "/api/**", "/webinar/**").authenticated()
                    .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                    .anyRequest().permitAll() //그게 아닌 모든 요청은 허용
                    .and()
                .formLogin() //로그인
                    .loginPage("/auth/signin") //로그인 페이지 / 인증이 필요하면 여기로 보냄  , GET
                    .defaultSuccessUrl("/")
                    .loginProcessingUrl("/auth/signin") //POST, 누군가가 해당 주소로 로그인 요청을 하면 얘가 낚아 챔, 스프링 시큐리티에게 로그인을 위임함
                    .failureUrl("/auth/signin?error")
                    .failureHandler(customLoginFailureHandler)
                    .and()
                .logout()
                    .logoutSuccessUrl("/");

    }
}
