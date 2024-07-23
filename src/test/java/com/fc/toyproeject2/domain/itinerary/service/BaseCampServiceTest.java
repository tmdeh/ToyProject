package com.fc.toyproeject2.domain.itinerary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fc.toyproeject2.domain.auth.model.entity.User;
import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.auth.repository.UserRepository;
import com.fc.toyproeject2.domain.itinerary.model.entity.BaseCamp;
import com.fc.toyproeject2.domain.itinerary.model.request.BaseCampRequest;
import com.fc.toyproeject2.domain.itinerary.repository.BaseCampRepository;
import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import com.fc.toyproeject2.domain.trip.repository.TripRepository;
import com.fc.toyproeject2.global.exception.type.ItineraryException;
import com.fc.toyproeject2.global.exception.type.ValidationException;
import com.fc.toyproeject2.global.util.KakaoMapUtil;
import com.fc.toyproeject2.global.exception.error.ValidationErrorCode;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
class BaseCampServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private BaseCampRepository baseCampRepository;

  @Mock
  private TripRepository tripRepository;

  @Mock
  private KakaoMapUtil kakaoMapUtil;

  @InjectMocks
  private BaseCampService baseCampService;

  @Mock
  private CustomUserDetails customUserDetails;

  @Test
  void deleteBaseCamp() {
    Long baseCampId = 1L;

    baseCampService.deleteBaseCamp(baseCampId, customUserDetails);

    verify(baseCampRepository, times(1)).deleteById(baseCampId);
  }

  @Test
  void saveBaseCamp() {
    Long tripId = 1L;
    BaseCampRequest request = new BaseCampRequest("Base Camp", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
    Trip trip = new Trip();
    String placeAddress = "Sample Address";

    when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));
    when(kakaoMapUtil.getPlaceAddress(request.name())).thenReturn(placeAddress);

    baseCampService.saveBaseCamp(tripId, request, customUserDetails);

    verify(baseCampRepository, times(1)).save(any(BaseCamp.class));
  }

  @Test
  void updateBaseCamp() {
    Long baseCampId = 1L;
    BaseCampRequest request = new BaseCampRequest("Updated Base Camp", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
    BaseCamp existingBaseCamp = new BaseCamp();

    String placeAddress = "Updated Address";

    when(baseCampRepository.findById(baseCampId)).thenReturn(Optional.of(existingBaseCamp));
    when(kakaoMapUtil.getPlaceAddress(request.name())).thenReturn(placeAddress);

    baseCampService.updateBaseCamp(baseCampId, request, customUserDetails);

    verify(baseCampRepository, times(1)).findById(baseCampId);
    assertEquals("Updated Base Camp", existingBaseCamp.getName());
    assertEquals("Updated Address", existingBaseCamp.getAddress());
  }

  @Test
  void saveBaseCampInvalidTime() {
    Long tripId = 1L;
    BaseCampRequest request = new BaseCampRequest("Base Camp", LocalDateTime.now().plusDays(1), LocalDateTime.now());

    ValidationException exception = assertThrows(ValidationException.class, () -> baseCampService.saveBaseCamp(tripId, request, customUserDetails));

    String actualMessage = exception.getStatusText();
    String expectedMessage = "시작 시간이 종료 시간보다 늦으면 안됩니다.";

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void updateBaseCampBaseCampNotFound() {
    Long baseCampId = 1L;
    BaseCampRequest request = new BaseCampRequest("Updated Base Camp", LocalDateTime.now(), LocalDateTime.now().plusDays(1));

    when(baseCampRepository.findById(baseCampId)).thenReturn(Optional.empty());

    ItineraryException exception = assertThrows(ItineraryException.class, () -> baseCampService.updateBaseCamp(baseCampId, request, customUserDetails));

    String actualMessage = exception.getStatusText();
    String expectedMessage = "Base Camp ID가 존재하지 않습니다.";

    assertEquals(expectedMessage, actualMessage);
  }
}
