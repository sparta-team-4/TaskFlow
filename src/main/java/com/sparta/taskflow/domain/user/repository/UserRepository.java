package com.sparta.taskflow.domain.user.repository;

import com.sparta.taskflow.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
}
