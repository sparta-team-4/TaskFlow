package com.sparta.taskflow.statistics.service;

public interface StatisticsinternalService {
    //통계
    //대시보드 통계 조회
    Statistics getDashboardStats();
    //팀 진행률 조회
    Statistics getTeamProgress();
    //내 작업 요약 조회
    Statistics getMyTask();
    //최근 활동 조회
    Page<Statistics> getActivities(int page, int size);


    //조회
    //통합 검색
    Statistics search(String query);
    //작업 검색
    Page<Statistics> searchTasks(String query, int page, int size);
}
