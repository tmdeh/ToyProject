package com.fc.toyproeject2.domain.itinerary.repository;

import com.fc.toyproeject2.domain.itinerary.model.entity.Stay;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StayRepository extends JpaRepository<Stay, Long> {
  List<Stay> findByTripId(Long tripId);
}