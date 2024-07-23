package com.fc.toyproeject2.domain.comment.service;

import com.fc.toyproeject2.domain.auth.model.entity.User;
import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.auth.repository.UserRepository;
import com.fc.toyproeject2.domain.comment.model.entity.Comment;
import com.fc.toyproeject2.domain.comment.model.request.CommentRequest;
import com.fc.toyproeject2.domain.comment.model.request.UpdateCommentRequest;
import com.fc.toyproeject2.domain.comment.model.response.CommentResponse;
import com.fc.toyproeject2.domain.comment.repository.CommentRepository;
import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import com.fc.toyproeject2.domain.trip.repository.TripRepository;
import com.fc.toyproeject2.global.annotaion.aspect.ValidationCommentInUserAspect;
import com.fc.toyproeject2.global.exception.type.CommentException;
import com.fc.toyproeject2.global.exception.type.TripReadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@EnableAspectJAutoProxy
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService; //@mock가 붙은 애들을 주입

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TripRepository tripRepository;


    @Mock
    private ValidationCommentInUserAspect validationCommentInUserAspect;

    private User user;
    private CustomUserDetails customUserDetails;
    private Trip trip;
    private Comment comment;


    @BeforeEach
    void setUp() {
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        CommentRepository commentRepositoryMock = Mockito.mock(CommentRepository.class);
        validationCommentInUserAspect = new ValidationCommentInUserAspect(userRepositoryMock, commentRepositoryMock);
        AspectJProxyFactory factory = new AspectJProxyFactory(commentService);
        factory.addAspect(validationCommentInUserAspect);
        commentService = factory.getProxy();

        user = new User(1L, "example@example.com", "1234", "test", null, null);

        customUserDetails = new CustomUserDetails(1L, "example@example.com", "1234");
        customUserDetails.setUser(user);

        trip = new Trip();

        comment = Comment.builder()
                .id(1L)
                .trip(trip)
                .user(user)
                .content("Test content")
                .createdAt(LocalDateTime.now())
                .build();


    }

    @Test
    void addComment_canAddComment() {
        CommentRequest request = new CommentRequest(trip.getId(), 1L, "Test", LocalDateTime.now());
        when(tripRepository.findById(request.tripId())).thenReturn(Optional.of(trip));//findbyid 시 해당 trip 반환
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponse response = commentService.addComment(request, customUserDetails);
        assertNotNull(response);
        assertEquals(comment.getId(), response.id());
        assertEquals(comment.getContent(), response.content());
        verify(commentRepository, times(1)).save(any(Comment.class));//save가 한번 실행됐는가
    }

    @Test
    void addComment_canThrowTripReadException() {
        CommentRequest request = new CommentRequest(trip.getId(), 1L, "Test", LocalDateTime.now());
        when(tripRepository.findById(request.tripId())).thenReturn(Optional.empty());//일치하는 trip x

        assertThrows(TripReadException.class, () -> commentService.addComment(request, customUserDetails));
    }

    @Test
    void getCommentsByTripId_canReturnComments() {
        when(commentRepository.findByTripId(trip.getId())).thenReturn(Collections.singletonList(comment));

        List<CommentResponse> responses = commentService.getCommentsByTripId(trip.getId());

        assertNotNull(responses);
        assertEquals(1, responses.size()); //댓글 1개인지
        assertEquals(comment.getId(), responses.get(0).id());//댓글 id 동일한지
    }

    @Test
    void deleteComment_canThrowCommentException_IfCommentNotExist() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());
        assertThrows(CommentException.class, () -> commentService.deleteComment(comment.getId(), customUserDetails));
    }


    @Test
    void updateCommentCanUpdateComment() {
        UpdateCommentRequest updateRequest = new UpdateCommentRequest("Updated");
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        CommentResponse response = commentService.updateComment(comment.getId(), customUserDetails, updateRequest);

        assertNotNull(response);
        assertEquals(updateRequest.getContent(), response.content());
    }

    @Test
    void updateComment_canThrowCommentExceptionIfNotFound() {
        UpdateCommentRequest updateRequest = new UpdateCommentRequest("Updated");
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());

        assertThrows(CommentException.class, () -> commentService.updateComment(comment.getId(), customUserDetails, updateRequest));
    }



}