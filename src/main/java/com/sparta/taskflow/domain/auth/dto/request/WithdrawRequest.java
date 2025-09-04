package com.sparta.taskflow.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record WithdrawRequest(
        @NotBlank(message = "비밀 번호는 필수 입력값입니다.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,}", message = "비밀번호는 8자 이상 영문, 숫자, 특수문자를 사용하세요.")
        String password
) {
}
