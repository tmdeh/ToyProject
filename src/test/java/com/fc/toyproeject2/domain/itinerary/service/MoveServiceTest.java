package com.fc.toyproeject2.domain.itinerary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.itinerary.model.entity.Move;
import com.fc.toyproeject2.domain.itinerary.model.request.MoveRequest;
import com.fc.toyproeject2.domain.itinerary.model.type.MoveType;
import com.fc.toyproeject2.domain.itinerary.repository.MoveRepository;
import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import com.fc.toyproeject2.domain.trip.repository.TripRepository;
import com.fc.toyproeject2.global.exception.type.ItineraryException;
import com.fc.toyproeject2.global.exception.type.ValidationException;
import com.fc.toyproeject2.global.util.KakaoMapUtil;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MoveServiceTest {

  @Mock
  private MoveRepository moveRepository;

  @Mock
  private TripRepository tripRepository;

  @Mock
  private KakaoMapUtil kakaoMapUtil;

  @InjectMocks
  private MoveService moveService;

  @Mock
  private CustomUserDetails customUserDetails;

  @Test
  void deleteMove() {
    Long moveId = 1L;

    moveService.deleteMove(moveId, customUserDetails);

    verify(moveRepository, times(1)).deleteById(moveId);
  }

  @Test
  void saveMove() {
    Long tripId = 1L;
    MoveRequest request = new MoveRequest(MoveType.BIKE, "Start Place", "End Place", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
    Trip trip = new Trip();
    String startPlaceAddress = "Start Address";
    String endPlaceAddress = "End Address";

    when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));
    when(kakaoMapUtil.getPlaceAddress(request.startPlace())).thenReturn(startPlaceAddress);
    when(kakaoMapUtil.getPlaceAddress(request.endPlace())).thenReturn(endPlaceAddress);

    moveService.saveMove(tripId, request, customUserDetails);

    verify(moveRepository, times(1)).save(any(Move.class));
  }

  @Test
  void updateMove() {
    Long moveId = 1L;
    MoveRequest request = new MoveRequest(MoveType.BIKE, "Updated Start Place", "Updated End Place", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
    Move existingMove = new Move();
    String startPlaceAddress = "Updated Start Address";
    String endPlaceAddress = "Updated End Address";

    when(moveRepository.findById(moveId)).thenReturn(Optional.of(existingMove));
    when(kakaoMapUtil.getPlaceAddress(request.startPlace())).thenReturn(startPlaceAddress);
    when(kakaoMapUtil.getPlaceAddress(request.endPlace())).thenReturn(endPlaceAddress);

    moveService.updateMove(moveId, request, customUserDetails);

    verify(moveRepository, times(1)).findById(moveId);
    assertEquals("Updated Start Place", existingMove.getStartPlace());
    assertEquals("Updated End Place", existingMove.getEndPlace());
    assertEquals("Updated Start Address", existingMove.getStartAddress());
    assertEquals("Updated End Address", existingMove.getEndAddress());
  }

  @Test
  void saveMoveInvalidTime() {
    Long tripId = 1L;
    MoveRequest request = new MoveRequest(MoveType.BIKE, "Start Place", "End Place", LocalDateTime.now().plusHours(1), LocalDateTime.now());

    ValidationException exception = assertThrows(ValidationException.class, () -> moveService.saveMove(tripId, request, customUserDetails));

    String actualMessage = exception.getStatusText();
    String expectedMessage = "시작 시간이 종료 시간보다 늦으면 안됩니다.";

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void updateMoveMoveNotFound() {
    Long moveId = 1L;
    MoveRequest request = new MoveRequest(MoveType.BIKE, "Updated Start Place", "Updated End Place", LocalDateTime.now(), LocalDateTime.now().plusHours(1));

    when(moveRepository.findById(moveId)).thenReturn(Optional.empty());

    ItineraryException exception = assertThrows(ItineraryException.class, () -> moveService.updateMove(moveId, request, customUserDetails));

    String actualMessage = exception.getStatusText();
    String expectedMessage = "Move ID가 존재하지 않습니다.";

    assertEquals(expectedMessage, actualMessage);
  }
}
