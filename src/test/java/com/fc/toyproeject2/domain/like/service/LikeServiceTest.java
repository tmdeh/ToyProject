package com.fc.toyproeject2.domain.like.service;

import com.fc.toyproeject2.domain.auth.model.entity.User;
import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.like.model.entity.TripLike;
import com.fc.toyproeject2.domain.like.repository.LikeRepository;
import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import com.fc.toyproeject2.domain.trip.model.response.TripLikedResponse;
import com.fc.toyproeject2.domain.trip.repository.TripRepository;
import com.fc.toyproeject2.global.exception.error.LikeErrorCode;
import com.fc.toyproeject2.global.exception.type.LikeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LikeServiceTest {

  @Mock
  private LikeRepository likeRepository;

  @Mock
  private TripRepository tripRepository;

  @InjectMocks
  private LikeService likeService;

  private CustomUserDetails customUserDetails;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    User user = new User(1L, "test@gmail.com", "1234", "test", null, null);
    customUserDetails = new CustomUserDetails(1L, "test@gmail.com", "1234");
    customUserDetails.setUser(user);
  }

  @Test
  void likeTripWhenTripNotFoundThenThrowLikeException() {
    Long tripId = anyLong();
    when(tripRepository.findById(tripId)).thenReturn(Optional.empty());
    LikeException exception = assertThrows(LikeException.class, () -> {
      likeService.likeTrip(customUserDetails, tripId);
    });
    assertEquals(LikeErrorCode.TRIP_NOT_FOUND.getInfo(), exception.getStatusText());
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
  }

  @Test
  void getUserLikedTripsWhenNoLikedTripsThenThrowLikeException() {
    String userEmail = anyString();
    when(likeRepository.findByUserEmail(userEmail)).thenReturn(Arrays.asList());

    LikeException exception = assertThrows(LikeException.class, () -> {
      likeService.getUserLikedTrips(customUserDetails);
    });
    assertEquals(LikeErrorCode.NO_LIKED_TRIPS.getInfo(), exception.getStatusText());
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
  }

  @Test
  void likeTripWhenTripFoundAndLikeExistsThenToggleLike() {
    Trip trip = new Trip();
    TripLike existingLike = new TripLike(1L, customUserDetails.getUser(), trip, true);

    when(tripRepository.findById(anyLong())).thenReturn(Optional.of(trip));
    when(likeRepository.findByUserIdAndTripId(anyLong(), anyLong())).thenReturn(Optional.of(existingLike));

    likeService.likeTrip(customUserDetails, 1L);

    assertFalse(existingLike.isLikeCheck());
    verify(likeRepository, never()).save(any());
  }

  @Test
  void likeTripWhenTripFoundAndLikeNotExistsThenSaveNewLike() {
    Trip trip = new Trip();
    when(tripRepository.findById(anyLong())).thenReturn(Optional.of(trip));
    when(likeRepository.findByUserIdAndTripId(anyLong(), anyLong())).thenReturn(Optional.empty());

    likeService.likeTrip(customUserDetails, 1L);

    ArgumentCaptor<TripLike> captor = ArgumentCaptor.forClass(TripLike.class);
    verify(likeRepository).save(captor.capture());

    TripLike savedLike = captor.getValue();
    assertNotNull(savedLike);
    assertTrue(savedLike.isLikeCheck());
    assertEquals(customUserDetails.getUser(), savedLike.getUser());
    assertEquals(trip, savedLike.getTrip());
  }

  @Test
  void getUserLikedTripsWhenLikedTripsExistThenReturnTripLikedResponses() {
    CustomUserDetails customUserDetails = mock(CustomUserDetails.class);

    // Trip 객체 생성
    Trip trip = Trip.builder()
        .id(1L)
        .name("Trip Name")
        .startTime(LocalDateTime.of(2024, 5, 30, 0, 0))
        .endTime(LocalDateTime.of(2024, 6, 5, 0, 0))
        .overseas(true)
        .build();

    // TripLike 객체 생성
    TripLike tripLike = new TripLike(1L, customUserDetails.getUser(), trip, true);
    when(customUserDetails.getEmail()).thenReturn("test@example.com");
    when(likeRepository.findByUserEmail(anyString())).thenReturn(Arrays.asList(tripLike));

    List<TripLikedResponse> responses = likeService.getUserLikedTrips(customUserDetails);

    assertNotNull(responses);
    assertEquals(1, responses.size());
    TripLikedResponse response = responses.get(0);

    // TripLikedResponse 객체의 getter 메소드를 이용하여 값 비교
    assertEquals(1L, response.getId());
    assertEquals("Trip Name", response.getName());
    assertEquals(LocalDateTime.of(2024, 5, 30, 0, 0), response.getStartTime());
    assertEquals(LocalDateTime.of(2024, 6, 5, 0, 0), response.getEndTime());
    assertTrue(response.getOverseas());
  }

  @Test
  void countLikesWhenCalledThenReturnCorrectCount() {
    when(likeRepository.countByTripId(anyLong())).thenReturn(5L);

    long likeCount = likeService.countLikes(1L);

    assertEquals(5L, likeCount);
  }
}
