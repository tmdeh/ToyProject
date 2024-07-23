package com.fc.toyproeject2.domain.itinerary.model.response;

import java.util.List;

public record ItineraryReadAllResponse(
        List<StayResponse> stays,
        List<MoveResponse> moves,
        List<BaseCampResponse> baseCamps
) {

}
