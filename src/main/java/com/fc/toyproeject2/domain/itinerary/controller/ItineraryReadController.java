package com.fc.toyproeject2.domain.itinerary.controller;

import com.fc.toyproeject2.domain.itinerary.model.response.ItineraryReadAllResponse;
import com.fc.toyproeject2.domain.itinerary.service.ItineraryReadService;
import com.fc.toyproeject2.global.util.APIUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/itinerary")
public class ItineraryReadController {

    private final ItineraryReadService itineraryReadService;

    @GetMapping(value = "/{tripId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllItinerariesByTripId(@PathVariable(name = "tripId") Long tripId) {
        return APIUtil.OK(itineraryReadService.getAllItinerariesByTripId(tripId));
    }

}
