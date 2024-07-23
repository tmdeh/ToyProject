package com.fc.toyproeject2.domain.trip.model.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TripLikedResponse {
  private Long id;
  private String name;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private Boolean overseas;
}
