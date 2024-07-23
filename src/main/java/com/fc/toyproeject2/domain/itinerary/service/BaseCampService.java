package com.fc.toyproeject2.domain.itinerary.service;

import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.itinerary.model.entity.BaseCamp;
import com.fc.toyproeject2.domain.itinerary.model.request.BaseCampRequest;
import com.fc.toyproeject2.domain.itinerary.repository.BaseCampRepository;
import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import com.fc.toyproeject2.domain.trip.repository.TripRepository;
import com.fc.toyproeject2.global.annotaion.ValidationBaseCampInUser;
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
public class BaseCampService {

    private final BaseCampRepository baseCampRepository;
    private final TripRepository tripRepository;
    private final KakaoMapUtil kakaoMapUtil;

    @ValidationBaseCampInUser
    @Transactional
    public void deleteBaseCamp(Long id, CustomUserDetails customUserDetails) {
        baseCampRepository.deleteById(id);
    }

    @ValidationTripInUser
    @Transactional
    public void saveBaseCamp(Long tripId, BaseCampRequest request, CustomUserDetails customUserDetails) {
        validateBaseCampRequest(request);
        validateTime(request.checkIn(), request.checkOut());

        String placeAddress = kakaoMapUtil.getPlaceAddress(request.name());

        baseCampRepository.save(
            new BaseCamp(
                null,
                getTrip(tripId),
                request.name(),
                placeAddress,
                request.checkIn(),
                request.checkOut()));
    }

    @ValidationBaseCampInUser
    @Transactional
    public void updateBaseCamp(Long id, BaseCampRequest request, CustomUserDetails customUserDetails) {
        BaseCamp existingBaseCamp = baseCampRepository.findById(id)
            .orElseThrow(() -> new ItineraryException(ItineraryErrorCode.NOT_FOUND_BASE_CAMP_ID));

        validateBaseCampRequest(request);
        validateTime(request.checkIn(), request.checkOut());

        String placeAddress = kakaoMapUtil.getPlaceAddress(request.name());

        existingBaseCamp.update(request.name(), placeAddress, request.checkIn(), request.checkOut());
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

    private void validateBaseCampRequest(BaseCampRequest request) {
        if (request == null ||
            StringUtils.isBlank(request.name()) ||
            request.checkIn() == null ||
            request.checkOut() == null) {
            throw new ValidationException(ItineraryErrorCode.EMPTY_ITINERARY_DATA);
        }
    }
}
