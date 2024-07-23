package com.fc.toyproeject2.domain.itinerary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.fc.toyproeject2.domain.itinerary.model.entity.BaseCamp;
import com.fc.toyproeject2.domain.itinerary.model.entity.Move;
import com.fc.toyproeject2.domain.itinerary.model.entity.Stay;
import com.fc.toyproeject2.domain.itinerary.model.response.BaseCampResponse;
import com.fc.toyproeject2.domain.itinerary.model.response.ItineraryReadAllResponse;
import com.fc.toyproeject2.domain.itinerary.model.response.MoveResponse;
import com.fc.toyproeject2.domain.itinerary.model.response.StayResponse;
import com.fc.toyproeject2.domain.itinerary.model.type.MoveType;
import com.fc.toyproeject2.domain.itinerary.repository.BaseCampRepository;
import com.fc.toyproeject2.domain.itinerary.repository.MoveRepository;
import com.fc.toyproeject2.domain.itinerary.repository.StayRepository;
import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import com.fc.toyproeject2.global.util.TimeUtil;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItineraryReadServiceTest {

  @Mock
  private StayRepository stayRepository;

  @Mock
  private MoveRepository moveRepository;

  @Mock
  private BaseCampRepository baseCampRepository;

  @InjectMocks
  private ItineraryReadService itineraryReadService;

  private Long tripId;
  private Stay stay;
  private Move move;
  private BaseCamp baseCamp;

  @BeforeEach
  void setUp() {
    tripId = 1L;
    Trip trip = Trip.builder()
        .id(tripId)
        .name("Test Trip")
        .startTime(LocalDateTime.now())
        .endTime(LocalDateTime.now().plusDays(1))
        .overseas(false)
        .build();

    stay = new Stay(1L, trip, "Stay Name", "Stay Address", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
    move = new Move(1L, trip, MoveType.BIKE, "Start Place", "Start Address", "End Place", "End Address", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
    baseCamp = new BaseCamp(1L, trip, "BaseCamp Name", "BaseCamp Address", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
  }

  @Test
  void getAllItinerariesByTripId() {
    // given
    when(stayRepository.findByTripId(tripId)).thenReturn(Collections.singletonList(stay));
    when(moveRepository.findByTripId(tripId)).thenReturn(Collections.singletonList(move));
    when(baseCampRepository.findByTripId(tripId)).thenReturn(Collections.singletonList(baseCamp));

    // when
    ItineraryReadAllResponse response = itineraryReadService.getAllItinerariesByTripId(tripId);

    // then
    List<StayResponse> expectedStays = Collections.singletonList(new StayResponse(stay.getId(), tripId, stay.getName(), stay.getAddress(), TimeUtil.localDateTimeToString(stay.getStartTime()), TimeUtil.localDateTimeToString(stay.getEndTime())));
    List<MoveResponse> expectedMoves = Collections.singletonList(new MoveResponse(move.getId(), tripId, move.getMoveType().getName(), move.getStartPlace(), move.getStartAddress(), move.getEndPlace(), move.getEndAddress(), TimeUtil.localDateTimeToString(move.getStartTime()), TimeUtil.localDateTimeToString(move.getEndTime())));
    List<BaseCampResponse> expectedBaseCamps = Collections.singletonList(new BaseCampResponse(baseCamp.getId(), tripId, baseCamp.getName(), baseCamp.getAddress(), TimeUtil.localDateTimeToString(baseCamp.getCheckIn()), TimeUtil.localDateTimeToString(baseCamp.getCheckOut())));

    assertEquals(expectedStays, response.stays());
    assertEquals(expectedMoves, response.moves());
    assertEquals(expectedBaseCamps, response.baseCamps());
  }
}
