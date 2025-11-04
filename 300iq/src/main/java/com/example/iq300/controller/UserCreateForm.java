package com.example.iq300.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
    
    @NotEmpty(message = "이메일은 필수 항목입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotEmpty(message = "닉네임은 필수 항목입니다.")
    @Size(min = 3, max = 25, message = "닉네임은 3자 이상 25자 이하로 입력해주세요.")
    private String nickname;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    private String password_1;

    @NotEmpty(message = "비밀번호 확인은 필수 항목입니다.")
    private String password_2;
}