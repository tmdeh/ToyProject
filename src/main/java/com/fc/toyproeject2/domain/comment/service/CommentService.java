package com.fc.toyproeject2.domain.comment.service;

import com.fc.toyproeject2.domain.auth.model.entity.User;
import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.comment.model.entity.Comment;
import com.fc.toyproeject2.domain.comment.model.request.CommentRequest;
import com.fc.toyproeject2.domain.comment.model.request.UpdateCommentRequest;
import com.fc.toyproeject2.domain.comment.model.response.CommentResponse;
import com.fc.toyproeject2.domain.comment.repository.CommentRepository;
import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import com.fc.toyproeject2.domain.trip.repository.TripRepository;
import com.fc.toyproeject2.global.annotaion.GetUser;
import com.fc.toyproeject2.global.annotaion.ValidationTripInUser;
import com.fc.toyproeject2.global.exception.error.CommentErrorCode;
import com.fc.toyproeject2.global.exception.error.TripReadErrorCode;
import com.fc.toyproeject2.global.exception.type.CommentException;
import com.fc.toyproeject2.global.exception.type.TripReadException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TripRepository tripRepository;

    @GetUser
    @Transactional
    public CommentResponse addComment(CommentRequest request, CustomUserDetails customUserDetails) {
        Trip trip = tripRepository.findById(request.tripId())
                .orElseThrow(() -> new TripReadException(TripReadErrorCode.NOT_FOUND_TRIP));

        User user = customUserDetails.getUser();

        Comment comment = Comment.builder()
                .trip(trip)
                .user(user)
                .user(customUserDetails.getUser())
                .content(request.content())
                .createdAt(LocalDateTime.now())
                .build();

        Comment savedComment = commentRepository.save(comment);

        return new CommentResponse(savedComment.getId(), savedComment.getTrip().getId(), savedComment.getUser().getId(), savedComment.getContent(), savedComment.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByTripId(Long tripId) {
        List<Comment> comments = commentRepository.findByTripId(tripId);
        return comments.stream()
                .map(comment -> new CommentResponse(comment.getId(), comment.getTrip().getId(),comment.getUser().getId(), comment.getContent(), comment.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @ValidationTripInUser
    public void deleteComment(Long id, CustomUserDetails customUserDetails) {
//        commentRepository.deleteById(id);
       Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
       commentRepository.delete(comment);
    }

    @ValidationTripInUser
    public CommentResponse updateComment(Long id, CustomUserDetails customUserDetails, UpdateCommentRequest content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        comment.setContent(content.getContent());
        commentRepository.save(comment);
        return new CommentResponse(comment.getId(), comment.getTrip().getId(), comment.getUser().getId(), comment.getContent(), comment.getCreatedAt());
    }

}
