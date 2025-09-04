package com.sparta.taskflow.domain.auth.dto.request;

import com.sparta.taskflow.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record UserRegisterRequest(
        @NotBlank(message = "로그인 ID는 필수입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "로그인 ID는 4~20자의 영문/숫자만 가능합니다.")
        String username,

        @NotBlank(message = "회원 이름은 필수입니다.")
        @Size(min = 2, max = 50, message = "이름은 2~50자 이내여야 합니다.")
        String name,

        @NotBlank(message = "이메일을 필수입니다.")
        @Email(message = "유효한 이메일 주소 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀 번호는 필수 입력값입니다.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,}", message = "비밀번호는 8자 이상 영문, 숫자, 특수문자를 사용하세요.")
        String password) {

    public User toEntity(String encodedPassword) {
        return User.register(username, email, encodedPassword, name);
    }
}
