package com.fc.toyproeject2.domain.itinerary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.itinerary.model.entity.Stay;
import com.fc.toyproeject2.domain.itinerary.model.request.StayRequest;
import com.fc.toyproeject2.domain.itinerary.repository.StayRepository;
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
class StayServiceTest {

  @Mock
  private StayRepository stayRepository;

  @Mock
  private TripRepository tripRepository;

  @Mock
  private KakaoMapUtil kakaoMapUtil;

  @InjectMocks
  private StayService stayService;

  @Mock
  private CustomUserDetails customUserDetails;

  @Test
  void deleteStay() {
    Long stayId = 1L;

    stayService.deleteStay(stayId, customUserDetails);

    verify(stayRepository, times(1)).deleteById(stayId);
  }

  @Test
  void saveStay() {
    Long tripId = 1L;
    StayRequest request = new StayRequest("Stay Name", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
    Trip trip = new Trip();
    String placeAddress = "Sample Address";

    when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));
    when(kakaoMapUtil.getPlaceAddress(request.name())).thenReturn(placeAddress);

    stayService.saveStay(tripId, request, customUserDetails);

    verify(stayRepository, times(1)).save(any(Stay.class));
  }

  @Test
  void updateStay() {
    Long stayId = 1L;
    StayRequest request = new StayRequest("Updated Stay Name", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
    Stay existingStay = new Stay();
    String placeAddress = "Updated Address";

    when(stayRepository.findById(stayId)).thenReturn(Optional.of(existingStay));
    when(kakaoMapUtil.getPlaceAddress(request.name())).thenReturn(placeAddress);

    stayService.updateStay(stayId, request, customUserDetails);

    verify(stayRepository, times(1)).findById(stayId);
    assertEquals("Updated Stay Name", existingStay.getName());
    assertEquals("Updated Address", existingStay.getAddress());
  }

  @Test
  void saveStayInvalidTime() {
    Long tripId = 1L;
    StayRequest request = new StayRequest("Stay Name", LocalDateTime.now().plusDays(1), LocalDateTime.now());

    ValidationException exception = assertThrows(ValidationException.class, () -> stayService.saveStay(tripId, request, customUserDetails));

    String actualMessage = exception.getStatusText();
    String expectedMessage = "시작 시간이 종료 시간보다 늦으면 안됩니다.";

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void updateStayStayNotFound() {
    Long stayId = 1L;
    StayRequest request = new StayRequest("Updated Stay Name", LocalDateTime.now(), LocalDateTime.now().plusDays(1));

    when(stayRepository.findById(stayId)).thenReturn(Optional.empty());

    ItineraryException exception = assertThrows(ItineraryException.class, () -> stayService.updateStay(stayId, request, customUserDetails));

    String actualMessage = exception.getStatusText();
    String expectedMessage = "Stay ID가 존재하지 않습니다.";

    assertEquals(expectedMessage, actualMessage);
  }
}
