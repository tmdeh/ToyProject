package com.fc.toyproeject2.domain.trip.model.request;

import java.time.LocalDate;

public record TripCreateRequest(
    String name,
    LocalDate startDate,
    LocalDate endDate,
    boolean overseas
) { }
