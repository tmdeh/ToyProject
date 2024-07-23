package com.fc.toyproeject2.domain.comment.model.response;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long tripId,
        Long userId,
        String content,
        LocalDateTime createdAt
) {
}
