package com.fc.toyproeject2.domain.trip.model.response;


import com.fc.toyproeject2.domain.comment.model.response.CommentResponse;

import java.util.List;

public record TripDetailResponse(
     Long id,
     String name,
     String startTime,
     String endTime,
     boolean overseas,
     List<CommentResponse> comments
) {

}
