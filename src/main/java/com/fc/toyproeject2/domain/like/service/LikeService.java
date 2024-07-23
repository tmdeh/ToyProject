package com.fc.toyproeject2.domain.like.service;


import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.like.model.entity.TripLike;
import com.fc.toyproeject2.domain.like.repository.LikeRepository;
import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import com.fc.toyproeject2.domain.trip.model.response.TripLikedResponse;
import com.fc.toyproeject2.domain.trip.repository.TripRepository;
import com.fc.toyproeject2.global.annotaion.GetUser;
import com.fc.toyproeject2.global.exception.error.LikeErrorCode;
import com.fc.toyproeject2.global.exception.type.LikeException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

  private final LikeRepository likeRepository;
  private final TripRepository tripRepository;

  @GetUser
  @Transactional
  public void likeTrip(CustomUserDetails customUserDetails, Long tripId) {
    Long userId = customUserDetails.getUser().getId();
    Trip trip = tripRepository.findById(tripId)
        .orElseThrow(() -> new LikeException(LikeErrorCode.TRIP_NOT_FOUND));

    likeRepository.findByUserIdAndTripId(userId, tripId)
        .ifPresentOrElse(
            like -> {
              like.setLikeCheck(!like.isLikeCheck());
            },
            () -> likeRepository.save(new TripLike(
                null,
                customUserDetails.getUser(),
                trip,
                true
            ))
        );

  }

  @Transactional(readOnly = true)
  public List<TripLikedResponse> getUserLikedTrips(CustomUserDetails customUserDetails) {
    String email = customUserDetails.getEmail();
    List<TripLike> likedTripLikes = likeRepository.findByUserEmail(email);

    if (likedTripLikes.isEmpty()) {
      throw new LikeException(LikeErrorCode.NO_LIKED_TRIPS);
    }

    return likedTripLikes.stream()
        .map(like -> {
          Trip trip = like.getTrip();
          return new TripLikedResponse(
              trip.getId(),
              trip.getName(),
              trip.getStartTime(),
              trip.getEndTime(),
              trip.getOverseas()
          );
        })
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public long countLikes(Long tripId) {
    return likeRepository.countByTripId(tripId);
  }
}