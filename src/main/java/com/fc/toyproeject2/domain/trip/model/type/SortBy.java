package com.fc.toyproeject2.domain.trip.model.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortBy {
    ID("id")
    ;
    private final String field;
}
