package com.sparta.taskflow.domain.user.repository;

import com.sparta.taskflow.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = """
            select count(*)
            from User
            where username = :username""",
            nativeQuery = true)
    Integer countByUsernameAndIsDeletedFalse(@Param("username") String username);

    @Query(value = """
            select count(*)
            from User
            where email = :email""",
            nativeQuery = true)
    Integer countByEmailAndIsDeletedFalse(@Param("email") String email);

    Optional<User> findByUsername(String username);
    List<User> findByIdNotIn(List<Long> userIds);

    List<User> findByNameContainingIgnoreCase(String query);
}
