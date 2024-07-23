package com.fc.toyproeject2.domain.itinerary.service;

import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.itinerary.model.entity.Move;
import com.fc.toyproeject2.domain.itinerary.model.request.MoveRequest;
import com.fc.toyproeject2.domain.itinerary.repository.MoveRepository;
import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import com.fc.toyproeject2.domain.trip.repository.TripRepository;
import com.fc.toyproeject2.global.annotaion.ValidationMoveInUser;
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
public class MoveService {

    private final MoveRepository moveRepository;
    private final TripRepository tripRepository;
    private final KakaoMapUtil kakaoMapUtil;

    @ValidationMoveInUser
    @Transactional
    public void deleteMove(Long id, CustomUserDetails customUserDetails) {
        moveRepository.deleteById(id);
    }

    @ValidationTripInUser
    @Transactional
    public void saveMove(Long tripId, MoveRequest request, CustomUserDetails customUserDetails) {
        validateMoveRequest(request);
        validateTime(request.startTime(), request.endTime());

        String startPlaceAddress = kakaoMapUtil.getPlaceAddress(request.startPlace());
        String endPlaceAddress = kakaoMapUtil.getPlaceAddress(request.endPlace());

        moveRepository.save(
            new Move(
                null,
                getTrip(tripId),
                request.moveType(),
                request.startPlace(),
                startPlaceAddress,
                request.endPlace(),
                endPlaceAddress,
                request.startTime(),
                request.endTime()
            )
        );
    }

    @ValidationMoveInUser
    @Transactional
    public void updateMove(Long id, MoveRequest request, CustomUserDetails customUserDetails) {
        Move existingMove = moveRepository.findById(id)
            .orElseThrow(() -> new ItineraryException(ItineraryErrorCode.NOT_FOUND_MOVE_ID));

        validateMoveRequest(request);
        validateTime(request.startTime(), request.endTime());

        String startPlaceAddress = kakaoMapUtil.getPlaceAddress(request.startPlace());
        String endPlaceAddress = kakaoMapUtil.getPlaceAddress(request.endPlace());

        existingMove.update(request.moveType(), request.startPlace(), startPlaceAddress, request.endPlace(), endPlaceAddress, request.startTime(), request.endTime());
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

    private void validateMoveRequest(MoveRequest request) {
        if (request == null ||
            request.moveType() == null ||
            StringUtils.isBlank(request.startPlace()) ||
            StringUtils.isBlank(request.endPlace()) ||
            request.startTime() == null ||
            request.endTime() == null) {
            throw new ValidationException(ItineraryErrorCode.EMPTY_ITINERARY_DATA);
        }
    }
}
