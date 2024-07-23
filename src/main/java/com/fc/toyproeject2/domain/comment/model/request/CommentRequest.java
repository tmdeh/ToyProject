package com.fc.toyproeject2.domain.comment.model.request;

import java.time.LocalDateTime;

public record CommentRequest(
        Long tripId,
        Long userId,
        String content,
        LocalDateTime createdAt
) {
}