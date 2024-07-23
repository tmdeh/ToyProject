package com.fc.toyproeject2.domain.itinerary.model.request;

import java.time.LocalDateTime;

public record BaseCampRequest(
    String name,
    LocalDateTime checkIn,
    LocalDateTime checkOut
) {}
