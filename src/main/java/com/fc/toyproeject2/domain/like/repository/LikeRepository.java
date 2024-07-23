package com.fc.toyproeject2.domain.like.repository;

import com.fc.toyproeject2.domain.like.model.entity.TripLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<TripLike, Long> {
  long countByTripId(Long tripId);
  Optional<TripLike> findByUserIdAndTripId(Long userId, Long tripId);

  List<TripLike> findByUserEmail(String email);
}