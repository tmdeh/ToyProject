package com.fc.toyproeject2.domain.itinerary.service;

import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.itinerary.model.entity.Stay;
import com.fc.toyproeject2.domain.itinerary.model.request.StayRequest;
import com.fc.toyproeject2.domain.itinerary.repository.StayRepository;
import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import com.fc.toyproeject2.domain.trip.repository.TripRepository;
import com.fc.toyproeject2.global.annotaion.ValidationTripInUser;
import com.fc.toyproeject2.global.exception.error.ItineraryErrorCode;
import com.fc.toyproeject2.global.exception.error.ValidationErrorCode;
import com.fc.toyproeject2.global.exception.type.ItineraryException;
import com.fc.toyproeject2.global.exception.type.ValidationException;
import com.fc.toyproeject2.global.util.KakaoMapUtil;
import com.fc.toyproeject2.global.util.ValidationUtil;
import io.micrometer.common.util.StringUtils;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StayService {

    private final StayRepository stayRepository;
    private final TripRepository tripRepository;
    private final KakaoMapUtil kakaoMapUtil;

    @ValidationTripInUser
    @Transactional
    public void saveStay(Long tripId, StayRequest request, CustomUserDetails customUserDetails) {
        validateStayRequest(request);
        validateTime(request.startTime(), request.endTime());

        String placeAddress = kakaoMapUtil.getPlaceAddress(request.name());

        stayRepository.save(
            new Stay(
                null,
                getTrip(tripId),
                request.name(),
                placeAddress,
                request.startTime(),
                request.endTime()
            )
        );
    }

    @ValidationTripInUser
    @Transactional
    public void updateStay(Long id, StayRequest request, CustomUserDetails customUserDetails) {
        Stay existingStay = stayRepository.findById(id)
            .orElseThrow(() -> new ItineraryException(ItineraryErrorCode.NOT_FOUND_STAY_ID));

        validateStayRequest(request);
        validateTime(request.startTime(), request.endTime());

        String placeAddress = kakaoMapUtil.getPlaceAddress(request.name());

        existingStay.update(request.name(), placeAddress, request.startTime(), request.endTime());
    }

    @ValidationTripInUser
    @Transactional
    public void deleteStay(Long id, CustomUserDetails customUserDetails) {
        stayRepository.deleteById(id);
    }

    private Trip getTrip(Long trip) {
        return tripRepository.findById(trip).orElseThrow(
            () -> new ItineraryException(ItineraryErrorCode.NOT_FOUND_TRIP_ID)
        );
    }

    private void validateTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (ValidationUtil.checkTime(startTime, endTime)) {
            throw new ValidationException(ValidationErrorCode.NOT_START_TIME_BEFORE_END_TIME);
        }
    }

    private void validateStayRequest(StayRequest request) {
        if (request == null ||
            StringUtils.isBlank(request.name()) ||
            request.startTime() == null ||
            request.endTime() == null) {
            throw new ValidationException(ItineraryErrorCode.EMPTY_ITINERARY_DATA);
        }
    }
}
