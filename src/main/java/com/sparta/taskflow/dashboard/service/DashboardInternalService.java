package com.sparta.taskflow.dashboard.service;

import java.util.List;

public interface DashboardInternalService {
    Dashboard getByIdOrThrow(Long id);
    List<Dashboard> getAllById(Long id);
    List<Dashboard> getAllByIds(List<Long> ids);
}
