package com.fc.toyproeject2.domain.trip.repository;

import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import com.fc.toyproeject2.domain.trip.model.dto.TripReadAllDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query("SELECT new com.fc.toyproeject2.domain.trip.model.dto.TripReadAllDto(t, COUNT(l.id), CASE WHEN (t.user.email" +
            " = :email AND l.LikeCheck = true) THEN true ELSE false END) " +
        "FROM Trip t " +
        "LEFT JOIN TripLike l ON t.id = l.trip.id " +
        "WHERE t.id < :id AND LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
        "GROUP BY t.id, t.user.id, l.LikeCheck " +
        "ORDER BY t.id DESC")
    Page<TripReadAllDto> findByIdLessThanAndNameContainingIgnoreCaseWithLikeCount(@Param("id") Long id, @Param("email") String email, @Param("keyword") String keyword, Pageable pageable);

    boolean existsByName(String name);

    long countByOverseasFalse(); //국내

    long countByOverseasTrue(); //해외
}