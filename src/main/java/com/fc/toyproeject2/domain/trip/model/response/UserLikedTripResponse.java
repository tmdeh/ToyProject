package com.fc.toyproeject2.domain.trip.model.response;

import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserLikedTripResponse {
  private List<Trip> likedTrips;

  public UserLikedTripResponse(List<Trip> likedTrips) {
    this.likedTrips = likedTrips;
  }
}