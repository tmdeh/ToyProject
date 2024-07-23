package com.fc.toyproeject2.domain.like.model.entity;

import com.fc.toyproeject2.domain.auth.model.entity.User;
import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TripLike {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  private Trip trip;

  @Column
  @Setter
  private boolean LikeCheck;

}