package com.fc.toyproeject2.domain.itinerary.model.response;

public record MoveResponse(
    Long id,
    Long tripId,
    String moveType,
    String startPlace,
    String startAddress,
    String endPlace,
    String endAddress,
    String startTime,
    String endTime
) {}