package com.fc.toyproeject2.domain.itinerary.repository;

import com.fc.toyproeject2.domain.itinerary.model.entity.BaseCamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseCampRepository extends JpaRepository<BaseCamp, Long> {
  List<BaseCamp> findByTripId(Long tripId);
}