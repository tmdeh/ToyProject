package com.fc.toyproeject2.domain.itinerary.service;

import static com.fc.toyproeject2.global.util.TimeUtil.localDateTimeToString;

import com.fc.toyproeject2.domain.itinerary.model.response.BaseCampResponse;
import com.fc.toyproeject2.domain.itinerary.model.response.ItineraryReadAllResponse;
import com.fc.toyproeject2.domain.itinerary.model.response.MoveResponse;
import com.fc.toyproeject2.domain.itinerary.model.response.StayResponse;
import com.fc.toyproeject2.domain.itinerary.repository.BaseCampRepository;
import com.fc.toyproeject2.domain.itinerary.repository.MoveRepository;
import com.fc.toyproeject2.domain.itinerary.repository.StayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItineraryReadService {

  private final BaseCampRepository baseCampRepository;
  private final MoveRepository moveRepository;
  private final StayRepository stayRepository;

  @Transactional(readOnly = true)
  public ItineraryReadAllResponse getAllItinerariesByTripId(Long tripId) {
    List<StayResponse> stays = stayRepository.findByTripId(tripId)
        .stream()
        .map(stay -> new StayResponse(stay.getId(), stay.getTrip().getId(), stay.getName(), stay.getAddress(), localDateTimeToString(stay.getStartTime()), localDateTimeToString(stay.getEndTime())))
        .collect(Collectors.toList());

    List<MoveResponse> moves = moveRepository.findByTripId(tripId)
        .stream()
        .map(move -> new MoveResponse(move.getId(), move.getTrip().getId(), move.getMoveType().getName(), move.getStartPlace(), move.getStartAddress(),move.getEndPlace(), move.getEndAddress(), localDateTimeToString(move.getStartTime()), localDateTimeToString(move.getEndTime())))
        .collect(Collectors.toList());

    List<BaseCampResponse> baseCamps = baseCampRepository.findByTripId(tripId)
        .stream()
        .map(baseCamp -> new BaseCampResponse(baseCamp.getId(), baseCamp.getTrip().getId(), baseCamp.getName(), baseCamp.getAddress(), localDateTimeToString(baseCamp.getCheckIn()), localDateTimeToString(baseCamp.getCheckOut())))
        .collect(Collectors.toList());

    return new ItineraryReadAllResponse(stays, moves, baseCamps);
  }

}
