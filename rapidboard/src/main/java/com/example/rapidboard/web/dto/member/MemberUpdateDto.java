package com.example.rapidboard.web.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class MemberUpdateDto {
//    @NotBlank(message = "Please enter your username.")
//    @Size(min = 2, max = 20)
//    private String username;

    @NotBlank(message = "Please enter your name")
    @Size(max = 255)
    private String name;

    @NotBlank(message = "Please enter your current password.")
    @Size(max = 100)
    private String currentPassword;

    @Size(max = 100)
    private String newPassword;
//
//    @NotBlank(message = "Please enter your email.")
//    @Size(max = 255)
//    private String email;
}
