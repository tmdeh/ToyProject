package com.fc.toyproeject2.domain.trip.model.dto;

import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TripReadAllDto {
    private Trip trip;
    private Long likeCount;
    private boolean myLike;
}
