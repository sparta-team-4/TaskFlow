package com.sparta.taskflow.domain.task.service;

import com.sparta.taskflow.domain.task.dto.StatusRequest;
import com.sparta.taskflow.domain.task.dto.TaskRequest;
import com.sparta.taskflow.domain.task.dto.TaskResponse;
import com.sparta.taskflow.domain.task.dto.UpdateRequest;
import com.sparta.taskflow.domain.task.entity.Task;
import com.sparta.taskflow.domain.task.enums.TaskPriority;
import com.sparta.taskflow.domain.task.exception.CustomException;
import com.sparta.taskflow.domain.task.exception.TaskErrorCode;
import com.sparta.taskflow.domain.task.repository.TaskRepository;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.exception.UserNotFoundException;
import com.sparta.taskflow.domain.user.service.internal.UserInternalService;
import com.sparta.taskflow.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.sparta.taskflow.domain.task.enums.TaskPriority.HIGH;
import static com.sparta.taskflow.domain.task.enums.TaskStatus.DONE;
import static com.sparta.taskflow.domain.task.enums.TaskStatus.TODO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserInternalService userInternalService;
    @InjectMocks
    private TaskService taskService;

    private LocalDateTime dueDate;
    private LocalDateTime date;
    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        dueDate = LocalDateTime.of(2025, 9, 7, 11, 0, 0);
        date = LocalDateTime.of(2025, 8, 7, 11, 0, 0);
        user = TestUtils.createEntity(User.class, Map.of("id", 1L));
        task = Task.builder()
                .title("title")
                .description("desc")
                .dueDate(LocalDateTime.now())
                .priority(TaskPriority.HIGH)
                .assignee(user)
                .isDeleted(false)
                .build();

        ReflectionTestUtils.setField(task, "id", 1L);
    }

    @Nested
    @DisplayName("Task 목록 조회")
    class GetByCategoryTest {
        @Test
        @DisplayName("조건에 맞는 Task 목록을 조회한다.")
        void getByCategory_success() {
            // given
            Long assigneeId = 1L;
            Pageable pageable = PageRequest.of(0, 10);
            Page<Task> taskPage = new PageImpl<>(List.of(task));

            given(taskRepository.search(TODO, "test", assigneeId, pageable)).willReturn(taskPage);

            // when
            Page<TaskResponse> responsePage = taskService.getByCategory(TODO, "test", assigneeId, pageable);

            // then
            assertThat(responsePage).hasSize(1);
            assertThat(responsePage.getContent().get(0).getId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("Task 상세 조회")
    class GetByIdTest {
        @Test
        @DisplayName("Task 상세 조회에 성공한다.")
        void getById_success() {
            // given
            Long taskId = 1L;
            given(taskRepository.findTaskByIdOrThrow(taskId)).willReturn(task);

            // when
            TaskResponse response = taskService.getById(taskId);

            // then
            assertThat(response.getId()).isEqualTo(taskId);
        }

        @Test
        @DisplayName("삭제된 Task는 상세 조회할 수 없다.")
        void getById_fail_whenTaskDeleted() {
            // given
            Long taskId = 1L;
            Task task = TestUtils.createEntity(Task.class,
                    Map.of("id", taskId, "isDeleted", true));
            given(taskRepository.findTaskByIdOrThrow(taskId)).willReturn(task);

            // when & then
            assertThatThrownBy(() -> taskService.getById(taskId))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(TaskErrorCode.DELETED_TASK.getMessage());
        }
    }

    @Nested
    @DisplayName("Task 생성")
    class CreateTaskTest {
        @Test
        @DisplayName("Task 생성에 성공한다.")
        void createTask_success() {
            // given
            Long assigneeId = 1L;
            TaskRequest request = new TaskRequest("title test", "desc", dueDate, HIGH, assigneeId);
            given(userInternalService.getUserByIdOrThrow(assigneeId)).willReturn(user);
            given(taskRepository.save(any(Task.class))).willReturn(task);

            // when
            TaskResponse response = taskService.create(request, 1L);

            // then
            assertThat(response).extracting("id", "title", "description", "dueDate", "priority", "assigneeId", "status", "assignee", "createdAt", "updatedAt").contains(1L, "title test", "desc", dueDate, HIGH, TODO, assigneeId, user, date, date);
            verify(taskRepository).save(any(Task.class));
        }

        @Test
        @DisplayName("관리자가 존재하지 않으면 Task 생성을 할 수 없다.")
        void createTask_fail_whenAssigneeNotExist() {
            // given
            Long assigneeId = 99L;
            TaskRequest request = new TaskRequest("title test", "desc", dueDate, HIGH, assigneeId);

            // when & then
            assertThatThrownBy(() -> taskService.create(request, 1L))
                    .isInstanceOf(UserNotFoundException.class);
            verify(taskRepository, never()).save(any(Task.class));
        }
    }

    @Nested
    @DisplayName("Task 수정")
    class UpdateTaskTest {
        @Test
        @DisplayName("Task 수정에 성공한다.")
        void updateTeam_success() {
            // given
            Long assigneeId = 1L;
            Long taskId = 1L;
            UpdateRequest request = new UpdateRequest("title update", "desc", dueDate, HIGH, DONE, assigneeId);
            given(taskRepository.findTaskByIdOrThrow(taskId)).willReturn(task);
            given(userInternalService.getUserByIdOrThrow(assigneeId)).willReturn(user);

            // when
            TaskResponse response = taskService.update(taskId, request);

            // then
            assertThat(response).extracting("id", "title", "description", "dueDate", "priority", "status", "assigneeId", "assignee", "createdAt", "updatedAt").contains(1L, "title update", "desc", dueDate, HIGH, DONE, assigneeId, user, date, date);
            verify(task).update("title update", "desc", HIGH, dueDate, DONE, user);
        }

        @Test
        @DisplayName("관리자가 존재하지 않으면 Task 수정을 할 수 없다.")
        void updateTask_fail_whenAssigneeNotExist() {
            // given
            Long assigneeId = 99L;
            Long taskId = 1L;
            UpdateRequest request = new UpdateRequest("title update", "desc", dueDate, HIGH, DONE, assigneeId);

            // when & then
            assertThatThrownBy(() -> taskService.update(taskId, request))
                    .isInstanceOf(UserNotFoundException.class);
            verify(taskRepository, never()).save(any(Task.class));
        }

        @Test
        @DisplayName("Task가 존재하지 않으면 Task 수정을 할 수 없다.")
        void updateTask_fail_whenTaskNotExist() {
            // given
            Long assigneeId = 1L;
            Long taskId = 99L;
            UpdateRequest request = new UpdateRequest("title update", "desc", dueDate, HIGH, DONE, assigneeId);
            given(taskRepository.findById(taskId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> taskService.update(taskId, request))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(TaskErrorCode.TASK_NOT_FOUND.getMessage());
            verify(taskRepository, never()).save(any(Task.class));
        }

        @Test
        @DisplayName("삭제된 Task는 Task 수정을 할 수 없다.")
        void updateTask_fail_whenTaskDeleted() {
            // given
            Long taskId = 1L;
            Long assigneeId = 1L;
            Task task = TestUtils.createEntity(Task.class,
                    Map.of("id", taskId, "isDeleted", true));
            UpdateRequest request = new UpdateRequest("title update", "desc", dueDate, HIGH, DONE, assigneeId);
            given(taskRepository.findTaskByIdOrThrow(taskId)).willReturn(task);

            // when & then
            assertThatThrownBy(() -> taskService.update(taskId, request))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(TaskErrorCode.DELETED_TASK.getMessage());
        }
    }

    @Nested
    @DisplayName("Task 상태 변경")
    class UpdateStatusTest {
        @Test
        @DisplayName("Task 상태 변경에 성공한다.")
        void updateStatus_success() {
            // given
            Long taskId = 1L;
            StatusRequest request = new StatusRequest(DONE);

            given(taskRepository.findTaskByIdOrThrow(taskId)).willReturn(task);

            // when
            TaskResponse response = taskService.updateStatus(taskId, request);

            // then
            assertThat(response.getStatus()).isEqualTo(DONE);
            verify(task).updateStatus(DONE);
            verify(task).recordEndDate(DONE);
        }

        @Test
        @DisplayName("삭제된 Task는 상태를 변경할 수 없다.")
        void updateStatusTask_fail_whenTaskDeleted() {
            // given
            Long taskId = 1L;
            Task task = spy(TestUtils.createEntity(Task.class,
                    Map.of("id", taskId, "status", TODO, "isDeleted", true)));
            StatusRequest request = new StatusRequest(DONE);

            given(taskRepository.findTaskByIdOrThrow(taskId)).willReturn(task);

            // when & then
            assertThatThrownBy(() -> taskService.updateStatus(taskId, request))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(TaskErrorCode.DELETED_TASK.getMessage());
        }
    }

    @Nested
    @DisplayName("Task 삭제")
    class DeleteTaskTest {
        @Test
        @DisplayName("Task 삭제에 성공한다.")
        void deleteTask_success() {
            // given
            Long taskId = 1L;
            given(taskRepository.findTaskByIdOrThrow(taskId)).willReturn(task);

            // when
            taskService.delete(taskId);

            // then
            verify(taskRepository).delete(task);
        }

        @Test
        @DisplayName("존재하지 않는 Task면 예외가 발생한다.")
        void deleteTask_fail_whenTaskNotFound() {
            // given
            Long taskId = 99L;
            given(taskRepository.findById(taskId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> taskService.delete(taskId))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(TaskErrorCode.TASK_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("삭제된 Task는 삭제할 수 없다.")
        void deleteTask_fail_whenTaskDeleted() {
            // given
            Long taskId = 1L;
            Task task = TestUtils.createEntity(Task.class,
                    Map.of("id", taskId, "isDeleted", true));
            given(taskRepository.findTaskByIdOrThrow(taskId)).willReturn(task);

            // when & then
            assertThatThrownBy(() -> taskService.delete(taskId))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(TaskErrorCode.DELETED_TASK.getMessage());
        }
    }
}
