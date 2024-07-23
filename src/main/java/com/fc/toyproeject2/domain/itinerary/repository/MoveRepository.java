package com.fc.toyproeject2.domain.itinerary.repository;

import com.fc.toyproeject2.domain.itinerary.model.entity.Move;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveRepository extends JpaRepository<Move, Long> {
  List<Move> findByTripId(Long tripId);
}