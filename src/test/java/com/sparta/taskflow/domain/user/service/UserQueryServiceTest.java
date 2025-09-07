package com.sparta.taskflow.domain.user.service;

import com.sparta.taskflow.common.enums.Role;
import com.sparta.taskflow.domain.user.dto.response.UserDetailsResponse;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.repository.UserRepository;
import com.sparta.taskflow.domain.user.service.external.UserQueryService;

import com.sparta.taskflow.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserQueryService userQueryService;

    @Test
    @DisplayName("회원 프로필 상세 조회")
    void getUserDetails_success() {
        // given
        Long userId = 1L;
        User user = TestUtils.createEntity(User.class, Map.of(
                "id", userId,
                "username", "test123",
                "email", "test@test.com",
                "name", "test",
                "role", Role.USER
        ));
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        UserDetailsResponse response = userQueryService.getUserDetails(userId);

        // then
        assertThat(response)
                .extracting("username", "email", "name", "role")
                .contains("test123", "test@test.com", "test", Role.USER);
    }

}