package com.sparta.taskflow.domain.team.service.internal;

import java.util.List;

public interface TeamInternalService {
    List<Long> findUserIdsByTeamId(Long teamId);
}