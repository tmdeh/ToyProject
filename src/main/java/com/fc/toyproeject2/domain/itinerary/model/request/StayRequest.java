package com.fc.toyproeject2.domain.itinerary.model.request;

import java.time.LocalDateTime;

public record StayRequest(
    String name,
    LocalDateTime startTime,
    LocalDateTime endTime
) {}
