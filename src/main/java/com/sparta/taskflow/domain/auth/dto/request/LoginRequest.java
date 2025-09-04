package com.sparta.taskflow.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
        @NotBlank(message = "로그인 ID는 필수입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "로그인 ID는 4~20자의 영문/숫자만 가능합니다.")
        String username,

        @NotBlank(message = "비밀 번호는 필수 입력값입니다.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,}", message = "비밀번호는 8자 이상 영문, 숫자, 특수문자를 사용하세요.")
        String password
) {
        /**
         * 로깅 시, 민감한 정보 유출을 방지합니다.
         */
        @Override
        public String toString() {
                return "LoginRequest{" + "username='" + username + '\'' + ", password='****'}";
        }
}
