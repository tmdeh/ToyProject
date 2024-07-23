package com.fc.toyproeject2.domain.trip.service;

import com.fc.toyproeject2.domain.like.service.LikeService;
import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.comment.model.response.CommentResponse;
import com.fc.toyproeject2.domain.comment.repository.CommentRepository;
import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import com.fc.toyproeject2.domain.trip.model.request.TripCreateRequest;
import com.fc.toyproeject2.domain.trip.model.request.TripUpdateRequest;
import com.fc.toyproeject2.domain.trip.model.response.TripDetailResponse;
import com.fc.toyproeject2.domain.trip.model.response.TripLikedResponse;
import com.fc.toyproeject2.domain.trip.model.response.TripRankResponse;
import com.fc.toyproeject2.domain.trip.model.dto.TripReadAllDto;
import com.fc.toyproeject2.domain.trip.model.response.TripReadAllResponse;
import com.fc.toyproeject2.domain.trip.repository.TripRepository;
import com.fc.toyproeject2.global.annotaion.GetUser;
import com.fc.toyproeject2.global.annotaion.ValidationTripInUser;
import com.fc.toyproeject2.global.exception.error.TripCreateErrorCode;
import com.fc.toyproeject2.global.exception.error.TripReadErrorCode;
import com.fc.toyproeject2.global.exception.error.TripUpdateErrorCode;
import com.fc.toyproeject2.global.exception.error.ValidationErrorCode;
import com.fc.toyproeject2.global.exception.type.TripCreateException;
import com.fc.toyproeject2.global.exception.type.TripReadException;
import com.fc.toyproeject2.global.exception.type.TripUpdateException;
import com.fc.toyproeject2.global.exception.type.ValidationException;
import com.fc.toyproeject2.global.util.ValidationUtil;
import com.fc.toyproeject2.global.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {


    private final TripRepository tripRepository;
    private final LikeService likeService;
    private final CommentRepository commentRepository;

    @GetUser
    @Transactional
    public void createTrip(TripCreateRequest tripCreateRequest, CustomUserDetails userDetails) {
        if (ValidationUtil.checkDate(tripCreateRequest.startDate(), tripCreateRequest.endDate())) {
            throw new ValidationException(ValidationErrorCode.NOT_START_DATE_BEFORE_END_DATE);
        }

        // 예외 상황을 체크하여 예외를 throw할 수 있음
        if (tripCreateRequest == null || tripCreateRequest.name() == null) {
            throw new TripCreateException(TripCreateErrorCode.EMPTY_TRIP_DATA);
        }

        // 데이터베이스에서 여행 이름이 이미 존재하는지 확인
        if (tripRepository.existsByName(tripCreateRequest.name())) {
            throw new TripCreateException(TripCreateErrorCode.DUPLICATE_TRIP_NAME);
        }


        tripRepository.save(new Trip(
            null,
            tripCreateRequest.name(),
            tripCreateRequest.startDate().atStartOfDay(),
            tripCreateRequest.endDate().atStartOfDay(),
            tripCreateRequest.overseas(),
            null,
            null,
            null,
            userDetails.getUser(),
            null,
            null
        ));
    }

    @Transactional(readOnly = true)
    public Page<TripReadAllResponse> readAll(CustomUserDetails userDetails, Long cursor, int size, String keyword) {
        if(cursor == null || cursor <= 0) {
            cursor = Long.MAX_VALUE;
        }
        String email = null;
        if(userDetails != null) {
            email = userDetails.getEmail();
        }

        Pageable pageable = PageRequest.of(0, size); // 변경된 부분

        Page<TripReadAllDto> page = tripRepository.findByIdLessThanAndNameContainingIgnoreCaseWithLikeCount(cursor,
            email, keyword, pageable);

        return page.map(this::mapToTripResponse);
    }

    private TripReadAllResponse mapToTripResponse(TripReadAllDto result) {
        return new TripReadAllResponse(
            result.getTrip().getId(),
            result.getTrip().getName(),
            TimeUtil.localDateTimeOnlyDateToString(result.getTrip().getStartTime()),
            TimeUtil.localDateTimeOnlyDateToString(result.getTrip().getEndTime()),
            result.getTrip().getOverseas(),
            result.getLikeCount(),
            result.isMyLike()
        );
    }

    @Transactional(readOnly = true)
    public List<TripLikedResponse> getUserLikedTrips(CustomUserDetails customUserDetails) {
        return likeService.getUserLikedTrips(customUserDetails);
    }

    @Transactional(readOnly = true)
    public TripDetailResponse read(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripReadException(TripReadErrorCode.NOT_FOUND_TRIP));

        List<CommentResponse> comments = commentRepository.findByTripId(tripId).stream()
                .map(comment -> new CommentResponse(comment.getId(), comment.getTrip().getId(), comment.getUser().getId(), comment.getContent(), comment.getCreatedAt()))
                .collect(Collectors.toList());

        return new TripDetailResponse(trip.getId(), trip.getName(), TimeUtil.localDateTimeOnlyDateToString(trip.getStartTime()),
            TimeUtil.localDateTimeOnlyDateToString(trip.getEndTime()), trip.getOverseas(),
            comments);
    }



    @ValidationTripInUser
    @Transactional
    public void updateTrip(Long id, TripUpdateRequest request, CustomUserDetails customUserDetails) {

        if (ValidationUtil.checkTime(request.startTime(), request.endTime()))
            throw new ValidationException(ValidationErrorCode.NOT_START_TIME_BEFORE_END_TIME);

        Trip trip = tripRepository.findById(id)
            .orElseThrow(() -> new TripUpdateException(TripUpdateErrorCode.TRIP_NOT_FOUND));

        trip.setName(request.name());
        trip.setStartTime(request.startTime());
        trip.setEndTime(request.endTime());
        trip.setOverseas(request.overseas());
    }



    @ValidationTripInUser
    @Transactional
    public void delete(Long id, CustomUserDetails customUserDetails) {
        tripRepository.deleteById(id);
    }

    public TripRankResponse TripCountComparison() {
        long domesticTripCount = tripRepository.countByOverseasFalse();
        long overseasTripCount = tripRepository.countByOverseasTrue();

        List<String> rank = new ArrayList<>();

        if (domesticTripCount < overseasTripCount) {
            rank.add("해외 여행");
            rank.add("국내 여행");
        } else {
            rank.add("국내 여행");
            rank.add("해외 여행");
        }

        return new TripRankResponse(rank);
    }

}