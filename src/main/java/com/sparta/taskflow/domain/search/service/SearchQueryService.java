package com.sparta.taskflow.domain.search.service;

import com.sparta.taskflow.domain.search.dto.response.SearchResponse;
import com.sparta.taskflow.domain.task.service.TaskInternalService;
import com.sparta.taskflow.domain.team.service.internal.TeamInternalService;
import com.sparta.taskflow.domain.user.service.internal.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchQueryService {

    private final TaskInternalService taskInternalService;
    private final UserInternalService userInternalService;
    private final TeamInternalService teamInternalService;

    public SearchResponse unifiedSearch(String query) {
        return SearchResponse.of(
                taskInternalService.searchTasksByQuery(query),
                userInternalService.searchUsersByQuery(query),
                teamInternalService.searchTeamsByQuery(query)
        );
    }
}
