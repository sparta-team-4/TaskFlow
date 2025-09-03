package com.sparta.taskflow.domain.team.entity;

import com.sparta.taskflow.common.BaseEntity;

import jakarta.persistence.*;

import lombok.AccessLevel;

import lombok.Getter;

import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
}
