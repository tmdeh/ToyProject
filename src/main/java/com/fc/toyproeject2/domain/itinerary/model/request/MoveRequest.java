package com.fc.toyproeject2.domain.itinerary.model.request;

import com.fc.toyproeject2.domain.itinerary.model.type.MoveType;

import java.time.LocalDateTime;

public record MoveRequest(
    MoveType moveType,
    String startPlace,
    String endPlace,
    LocalDateTime startTime,
    LocalDateTime endTime
) {}
