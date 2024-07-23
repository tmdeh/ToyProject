package com.fc.toyproeject2.domain.trip.model.request;

import java.time.LocalDateTime;

public record TripUpdateRequest(
        Long id,
        String name,
        LocalDateTime startTime,
        LocalDateTime endTime,
        boolean overseas
) {

}
