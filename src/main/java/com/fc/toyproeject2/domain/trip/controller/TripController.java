package com.fc.toyproeject2.domain.trip.controller;

import com.fc.toyproeject2.domain.like.service.LikeService;
import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.trip.model.request.TripCreateRequest;
import com.fc.toyproeject2.domain.trip.model.request.TripUpdateRequest;
import com.fc.toyproeject2.domain.trip.service.TripService;
import com.fc.toyproeject2.global.util.APIUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trip")
public class TripController {

    private final TripService tripService;
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity createTrip(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody TripCreateRequest tripCreateRequest
    ) {
        tripService.createTrip(tripCreateRequest, customUserDetails);
        return APIUtil.OK();
    }

    @GetMapping
    public ResponseEntity getTrips(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "size") int size,
            @RequestParam(name = "cursor") Long cursor
    ) {
        return APIUtil.OK(tripService.readAll(customUserDetails, cursor, size, keyword));
    }

    @GetMapping("/liked")
    public ResponseEntity getUserLikedTrips(
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return APIUtil.OK(tripService.getUserLikedTrips(customUserDetails));
    }

    @GetMapping("/{id}")
    public ResponseEntity findTripById(@PathVariable(name = "id") Long id) {
        return APIUtil.OK(tripService.read(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTrip(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable(name = "id") Long id,
            @RequestBody TripUpdateRequest request
    ) {
        tripService.updateTrip(id, request, customUserDetails);
        return APIUtil.OK();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable(name = "id") Long id
    ) {
        tripService.delete(id,customUserDetails);
        return APIUtil.OK();
    }

    @GetMapping("/rank")
    public ResponseEntity getTripCounts() {
        return APIUtil.OK(tripService.TripCountComparison());
    }

}
