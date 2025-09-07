package com.sparta.taskflow.domain.comment.service;

import com.sparta.taskflow.domain.comment.dto.CommentRequest;
import com.sparta.taskflow.domain.comment.dto.CommentResponse;
import com.sparta.taskflow.domain.comment.dto.UpdateRequest;
import com.sparta.taskflow.domain.comment.entity.Comment;
import com.sparta.taskflow.domain.comment.exception.CommentErrorCode;
import com.sparta.taskflow.domain.comment.exception.CommentException;
import com.sparta.taskflow.domain.comment.repository.CommentRepository;
import com.sparta.taskflow.domain.task.entity.Task;
import com.sparta.taskflow.domain.task.exception.CustomException;
import com.sparta.taskflow.domain.task.exception.TaskErrorCode;
import com.sparta.taskflow.domain.task.service.TaskInternalService;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.service.internal.UserInternalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static com.sparta.taskflow.domain.comment.exception.CommentErrorCode.COMMENT_NOT_FOUND;
import static com.sparta.taskflow.domain.comment.exception.CommentErrorCode.OWNER_MISMATCH;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
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

        user = User.builder()
                .username("testUser")
                .name("test name")
                .email("test@test.com")
                .build();

        task = Task.builder()
                .title("title test")
                .build();

        comment = Comment.builder()
                .user(user)
                .task(task)
                .content("comment test")
                .build();

        ReflectionTestUtils.setField(comment, "id", 1L);
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
        assertThat(response.getContent().get(0).getContent()).isEqualTo("comment test");
    }

    @Nested
    @DisplayName("댓글 생성")
    class CreateCommentTest {
        @Test
        @DisplayName("댓글 생성 성공")
        void create_success() {
            // given
            CommentRequest request = new CommentRequest("comment test", null);

            given(taskInternalService.getByIdOrThrow(task.getId())).willReturn(task);
            given(userInternalService.getByIdOrThrow(user.getId())).willReturn(user);
            given(commentRepository.save(any(Comment.class))).willReturn(comment);

            // when
            CommentResponse response = commentExternalService.create(user.getId(), task.getId(), request);

            // then
            assertThat(response.getContent()).isEqualTo("comment test"); // save된 comment 반환
            verify(commentRepository, times(1)).save(any(Comment.class));
        }

        @Test
        @DisplayName("Task가 존재하지 않으면 댓글을 생성할 수 없다.")
        void create_fail_whenTaskNotExist() {
            Long taskId = 1L;
            CommentRequest request = new CommentRequest("comment test", null);

            given(taskInternalService.getByIdOrThrow(taskId))
                    .willThrow(new CustomException(TaskErrorCode.TASK_NOT_FOUND));


            assertThatThrownBy(() -> commentExternalService.create(1L, taskId, request))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(TaskErrorCode.TASK_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("댓글 수정")
    class UpdateCommentTest {
        @Test
        @DisplayName("댓글 수정 성공")
        void update_success() {
            // given
            UpdateRequest updateRequest = new UpdateRequest("comment update");

            given(taskInternalService.getByIdOrThrow(task.getId())).willReturn(task);
            given(commentRepository.findByIdOrThrow(comment.getId())).willReturn(comment);

            // when
            CommentResponse response = commentExternalService.update(user.getId(), task.getId(), comment.getId(), updateRequest);

            // then
            assertThat(response.getContent()).isEqualTo("comment update");
        }

        @Test
        @DisplayName("작성자가 일치하지 않으면 수정을 할 수 없다.")
        void update_fail_ownerMismatch() {
            // given
            UpdateRequest updateRequest = new UpdateRequest("comment update");
            Long anotherUserId = 99L;

            given(taskInternalService.getByIdOrThrow(task.getId())).willReturn(task);
            given(commentRepository.findByIdOrThrow(comment.getId())).willReturn(comment);

            // when & then
            assertThatThrownBy(() ->
                    commentExternalService.update(anotherUserId, task.getId(), comment.getId(), updateRequest))
                    .isInstanceOf(CommentException.class)
                    .hasMessage(OWNER_MISMATCH.getMessage());
        }

        @Test
        @DisplayName("댓글이 존재하지 않으면 예외가 발생한다.")
        void update_fail_whenCommentNotFound() {
            // given
            Long taskId = 1L;
            Long commentId = 99L;

            UpdateRequest updateRequest = new UpdateRequest("comment update");
            given(taskInternalService.getByIdOrThrow(taskId)).willReturn(task);
            given(commentRepository.findByIdOrThrow(commentId))
                    .willThrow(new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

            // when & then
            assertThatThrownBy(() -> commentExternalService.update(1L, taskId, commentId, updateRequest))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining(COMMENT_NOT_FOUND.getMessage());
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
            verify(commentRepository).delete(comment);
        }

        @Test
        @DisplayName("작성자가 일치하지 않으면 수정을 할 수 없다.")
        void update_fail_ownerMismatch() {
            // given
            UpdateRequest updateRequest = new UpdateRequest("comment update");
            Long anotherUserId = 99L;

            given(taskInternalService.getByIdOrThrow(task.getId())).willReturn(task);
            given(commentRepository.findByIdOrThrow(comment.getId())).willReturn(comment);

            // when & then
            assertThatThrownBy(() ->
                    commentExternalService.update(anotherUserId, task.getId(), comment.getId(), updateRequest))
                    .isInstanceOf(CommentException.class)
                    .hasMessage(OWNER_MISMATCH.getMessage());
        }

        @Test
        @DisplayName("댓글이 존재하지 않으면 예외가 발생한다.")
        void delete_fail_whenCommentNotFound() {
            // given
            Long taskId = 1L;
            Long commentId = 99L;

            given(taskInternalService.getByIdOrThrow(taskId)).willReturn(task);
            given(commentRepository.findByIdOrThrow(commentId))
                    .willThrow(new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

            // when & then
            assertThatThrownBy(() -> commentExternalService.delete(1L, taskId, commentId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining(COMMENT_NOT_FOUND.getMessage());
        }
    }
}
