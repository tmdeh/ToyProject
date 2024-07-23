package com.fc.toyproeject2.domain.trip.model.response;

public record TripReadAllResponse(
    Long id,
    String name,
    String startTime,
    String endTime,
    Boolean overseas,
    Long likeCount,
    Boolean myLiked
) {
}
