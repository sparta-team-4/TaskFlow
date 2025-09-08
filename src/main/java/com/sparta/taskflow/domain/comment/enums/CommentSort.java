package com.sparta.taskflow.domain.comment.enums;

import lombok.Getter;

@Getter
public enum CommentSort {
    NEWEST("newest"),
    OLDEST("oldest");

    private final String sortType;

    CommentSort(String sortType) {
        this.sortType = sortType;
    }

    public static CommentSort from(String value) {
        for (CommentSort sort : values()) {
            if (sort.name().equalsIgnoreCase(value) || sort.getSortType().equalsIgnoreCase(value)) {
                return sort;
            }
        }
        return NEWEST; // 기본값
    }
}

