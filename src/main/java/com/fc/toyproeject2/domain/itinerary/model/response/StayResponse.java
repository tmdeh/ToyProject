package com.fc.toyproeject2.domain.itinerary.model.response;

public record StayResponse(
   Long id,
   Long tripId,
   String name,
   String address,
   String startTime,
   String endTime
) {}
