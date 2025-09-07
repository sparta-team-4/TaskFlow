package com.sparta.taskflow.domain.comment.service;

import com.sparta.taskflow.domain.comment.dto.CommentRequest;
import com.sparta.taskflow.domain.comment.dto.CommentResponse;
import com.sparta.taskflow.domain.comment.dto.UpdateRequest;
import com.sparta.taskflow.domain.comment.entity.Comment;
import com.sparta.taskflow.domain.comment.exception.CommentException;
import com.sparta.taskflow.domain.comment.repository.CommentRepository;
import com.sparta.taskflow.domain.task.entity.Task;
import com.sparta.taskflow.domain.task.service.TaskInternalService;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.service.UserInternalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.List;

import static com.sparta.taskflow.domain.comment.exception.CommentErrorCode.OWNER_MISMATCH;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserInternalService userInternalService;
    @Mock
    private TaskInternalService taskInternalService;

    @InjectMocks
    private CommentExternalService commentExternalService;

    private User user;
    private Task task;
    private Comment comment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .username("testUser")
                .name("홍길동")
                .email("test@test.com")
                .build();

        task = Task.builder()
                .id(1L)
                .title("테스트 태스크")
                .build();

        comment = Comment.builder()
                .user(user)
                .task(task)
                .content("첫 댓글")
                .build();

        // 리플렉션으로 ID 강제 세팅 (테스트용)
        TestUtils.setField(comment, "id", 1L);
    }

    @Test
    @DisplayName("댓글 목록 조회 성공")
    void getCommentByPage_success() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<Comment> page = new PageImpl<>(List.of(comment), pageable, 1);

        given(taskInternalService.getByIdOrThrow(task.getId())).willReturn(task);
        given(commentRepository.findAllByTaskId(task.getId(), pageable)).willReturn(page);

        // when
        Page<CommentResponse> response = commentExternalService.getCommentByPage(task.getId(), pageable);

        // then
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getContent()).isEqualTo("첫 댓글");
    }

    @Test
    @DisplayName("댓글 생성 성공")
    void create_success() {
        // given
        CommentRequest request = new CommentRequest("새 댓글", null);

        given(taskInternalService.getByIdOrThrow(task.getId())).willReturn(task);
        given(userInternalService.getByIdOrThrow(user.getId())).willReturn(user);
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        // when
        CommentResponse response = commentExternalService.create(user.getId(), task.getId(), request);

        // then
        assertThat(response.getContent()).isEqualTo("첫 댓글"); // save된 comment 반환
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Nested
    @DisplayName("댓글 수정")
    class DeleteCommentTest {
        @Test
        @DisplayName("댓글 수정 성공")
        void update_success() {
            // given
            UpdateRequest updateRequest = new UpdateRequest("수정된 댓글");

            given(taskInternalService.getByIdOrThrow(task.getId())).willReturn(task);
            given(commentRepository.findByIdOrThrow(comment.getId())).willReturn(comment);

            // when
            CommentResponse response = commentExternalService.update(user.getId(), task.getId(), comment.getId(), updateRequest);

            // then
            assertThat(response.getContent()).isEqualTo("수정된 댓글");
        }

        @Test
        @DisplayName("작성자가 일치하지 않으면 수정을 할 수 없다.")
        void update_fail_ownerMismatch() {
            // given
            UpdateRequest updateRequest = new UpdateRequest("수정된 댓글");
            Long anotherUserId = 99L;

            given(taskInternalService.getByIdOrThrow(task.getId())).willReturn(task);
            given(commentRepository.findByIdOrThrow(comment.getId())).willReturn(comment);

            // when & then
            assertThatThrownBy(() ->
                    commentExternalService.update(anotherUserId, task.getId(), comment.getId(), updateRequest))
                    .isInstanceOf(CommentException.class)
                    .hasMessage(OWNER_MISMATCH.getMessage());
        }
    }

    @Nested
    @DisplayName("댓글 삭제")
    class DeleteCommentTest {
        @Test
        @DisplayName("댓글 삭제 성공")
        void delete_success() {
            // given
            given(taskInternalService.getByIdOrThrow(task.getId())).willReturn(task);
            given(commentRepository.findByIdOrThrow(comment.getId())).willReturn(comment);

            // when
            commentExternalService.delete(user.getId(), task.getId(), comment.getId());

            // then
            verify(commentRepository, times(1)).delete(comment);
        }

        @Test
        @DisplayName("작성자가 일치하지 않으면 수정을 할 수 없다.")
        void update_fail_ownerMismatch() {
            // given
            UpdateRequest updateRequest = new UpdateRequest("수정된 댓글");
            Long anotherUserId = 99L;

            given(taskInternalService.getByIdOrThrow(task.getId())).willReturn(task);
            given(commentRepository.findByIdOrThrow(comment.getId())).willReturn(comment);

            // when & then
            assertThatThrownBy(() ->
                    commentExternalService.update(anotherUserId, task.getId(), comment.getId(), updateRequest))
                    .isInstanceOf(CommentException.class)
                    .hasMessage(OWNER_MISMATCH.getMessage());
        }
    }
}
