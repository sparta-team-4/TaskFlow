package com.sparta.taskflow.common.logs.annotation;

import com.sparta.taskflow.domain.activitylog.enums.ActivityType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
    ActivityType value();
}
