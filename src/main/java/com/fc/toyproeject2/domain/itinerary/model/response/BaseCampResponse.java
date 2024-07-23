package com.fc.toyproeject2.domain.itinerary.model.response;

public record BaseCampResponse(
    Long id,
    Long tripId,
    String name,
    String address,
    String checkIn,
    String checkOut
) {}
