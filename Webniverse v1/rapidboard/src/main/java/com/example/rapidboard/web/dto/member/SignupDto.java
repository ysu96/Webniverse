package com.example.rapidboard.web.dto.member;

import com.example.rapidboard.domain.member.Member;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignupDto {

    private String username;

    private String password;

    private String name;

    private String email;

    private String role;

    private Integer isValidateCheck;

    public Member toEntity() {
        return Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .name(name)
                .role(role)
                .build();
    }
}
