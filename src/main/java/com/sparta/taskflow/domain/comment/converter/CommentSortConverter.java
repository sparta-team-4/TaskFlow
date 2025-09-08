package com.sparta.taskflow.domain.comment.converter;

import com.sparta.taskflow.domain.comment.enums.CommentSort;
import jakarta.annotation.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommentSortConverter implements Converter<String, CommentSort> {
    @Override
    public CommentSort convert(@Nullable String source) {
        return CommentSort.from(source);
    }
}
