package com.fc.toyproeject2.domain.trip.service;

import com.fc.toyproeject2.domain.auth.model.entity.User;
import com.fc.toyproeject2.domain.auth.model.userdetails.CustomUserDetails;
import com.fc.toyproeject2.domain.comment.repository.CommentRepository;
import com.fc.toyproeject2.domain.like.model.entity.TripLike;
import com.fc.toyproeject2.domain.trip.model.dto.TripReadAllDto;
import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import com.fc.toyproeject2.domain.trip.model.request.TripCreateRequest;
import com.fc.toyproeject2.domain.trip.model.request.TripUpdateRequest;
import com.fc.toyproeject2.domain.trip.model.response.TripDetailResponse;
import com.fc.toyproeject2.domain.trip.model.response.TripRankResponse;
import com.fc.toyproeject2.domain.trip.model.response.TripReadAllResponse;
import com.fc.toyproeject2.domain.trip.repository.TripRepository;
import com.fc.toyproeject2.global.exception.error.TripCreateErrorCode;
import com.fc.toyproeject2.global.exception.error.TripReadErrorCode;
import com.fc.toyproeject2.global.exception.error.TripUpdateErrorCode;
import com.fc.toyproeject2.global.exception.type.TripCreateException;
import com.fc.toyproeject2.global.exception.type.TripReadException;
import com.fc.toyproeject2.global.exception.type.TripUpdateException;
import com.fc.toyproeject2.global.util.TimeUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TripServiceTest {

    @Mock
    private TripRepository tripRepository;


    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private TripService tripService;

    private CustomUserDetails userDetails;

    private List<Trip> trips;

    private List<TripLike> tripLikes;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        trips = new ArrayList<>();
        tripLikes = new ArrayList<>();
        userDetails = new CustomUserDetails(1L, "test@example.com", null);
        User user = new User(1L, "test@example.com", "1234", "test", trips, tripLikes);
        userDetails.setUser(user);
        for (long i = 0; i < 10; i++) {
            Trip trip = new Trip(i, "test" + i, LocalDateTime.now().plusDays(i),
                LocalDateTime.now().plusDays(i).plusDays(3),
                false, null, null, null, userDetails.getUser(), tripLikes, null);
            TripLike tripLike = new TripLike(i, user, trip, i % 2 == 0);
            tripLikes.add(tripLike);
            trips.add(trip);
        }


    }


    @Test
    public void testCreateTrip_Success() {
        // given
        TripCreateRequest request = new TripCreateRequest("Test Trip", LocalDate.now(), LocalDate.now().plusDays(3), false);

        // when
        assertDoesNotThrow(() -> tripService.createTrip(request, userDetails));

        // then
        verify(tripRepository, times(1)).existsByName("Test Trip");
        verify(tripRepository, times(1)).save(any(Trip.class));
    }

    @Test
    public void testCreateTrip_WithExistingName() {
        // Arrange
        TripCreateRequest request = new TripCreateRequest("Existing Trip", LocalDate.now(), LocalDate.now().plusDays(3), false);

        // Mocking repository method
        when(tripRepository.existsByName("Existing Trip")).thenReturn(true);

        // Act & Assert
        TripCreateException exception = assertThrows(TripCreateException.class, () -> tripService.createTrip(request, userDetails));
        assertEquals(TripCreateErrorCode.DUPLICATE_TRIP_NAME.getCode(), exception.getStatusCode());

        // Verify
        verify(tripRepository, times(1)).existsByName("Existing Trip");
        verify(tripRepository, never()).save(any(Trip.class));
    }


    @Test
    public void testReadAll() {
        // Given
        Long cursor = 1L;
        int size = 10;
        String keyword = "test";

        List<TripReadAllDto> tripReadAllDtoList = new ArrayList<>();
        for (int i = 0; i < trips.size(); i++) {
            Trip trip = trips.get(i);
            boolean myLike = tripLikes.get(i).isLikeCheck();
            tripReadAllDtoList.add(new TripReadAllDto(trip, 0L, myLike));
        }

        Page<TripReadAllDto> page = new PageImpl<>(tripReadAllDtoList);
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getEmail()).thenReturn("test@example.com");

        // Mock the repository method
        when(tripRepository.findByIdLessThanAndNameContainingIgnoreCaseWithLikeCount(anyLong(), anyString(), anyString(), any(Pageable.class)))
            .thenReturn(page);

        // When
        Page<TripReadAllResponse> result = tripService.readAll(userDetails, cursor, size, keyword);


        // Then
        assertNotNull(result);
        assertEquals(10, result.getSize());
        assertEquals(tripReadAllDtoList.size(), result.getContent().size());

        for (int i = 0; i < result.getContent().size(); i++) {
            TripReadAllResponse response = result.getContent().get(i);
            Trip trip = trips.get(i);
            assertEquals(trip.getId(), response.id());
            assertEquals(trip.getName(), response.name());
            assertEquals(TimeUtil.localDateTimeOnlyDateToString(trip.getStartTime()), response.startTime());
            assertEquals(TimeUtil.localDateTimeOnlyDateToString(trip.getEndTime()), response.endTime());
            assertEquals(trip.getOverseas(), response.overseas());
            assertEquals(0L, response.likeCount());
            assertEquals(tripLikes.get(i).isLikeCheck(), response.myLiked());
        }

        // Verify interactions
        verify(tripRepository, times(1)).findByIdLessThanAndNameContainingIgnoreCaseWithLikeCount(anyLong(), anyString(), anyString(), any(Pageable.class));
    }


    @Test
    public void testReadAll_EmptyPage() {
        // Arrange
        Long cursor = 1L;
        int size = 10;
        String keyword = "test";

        when(tripRepository.findByIdLessThanAndNameContainingIgnoreCaseWithLikeCount(any(), any(), any(), any()))
            .thenReturn(Page.empty());

        // Act
        Page<TripReadAllResponse> result = tripService.readAll(userDetails, cursor, size, keyword);

        // Assert
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    public void testRead() {
        // Arrange
        Long tripId = 1L;
        Trip trip = new Trip(tripId, "Test Trip", LocalDate.now().atStartOfDay(), LocalDate.now().plusDays(3).atStartOfDay(),
            false, null, null, null,userDetails.getUser(), null, null);

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));
        when(commentRepository.findByTripId(tripId)).thenReturn(Collections.emptyList());

        // Act
        TripDetailResponse response = tripService.read(tripId);

        // Assert
        assertNotNull(response);
        assertEquals(tripId, response.id());
        assertEquals("Test Trip", response.name());
        assertEquals(TimeUtil.localDateTimeOnlyDateToString(trip.getStartTime()), response.startTime());
        assertEquals(TimeUtil.localDateTimeOnlyDateToString(trip.getEndTime()), response.endTime());
        assertFalse(response.overseas());
        assertTrue(response.comments().isEmpty());
    }

    @Test
    public void testRead_NotFound() {
        // given
        Long tripId = 1L;
        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        // when
        TripReadException exception = assertThrows(TripReadException.class, () -> tripService.read(tripId));

        // then
        assertEquals(TripReadErrorCode.NOT_FOUND_TRIP.getCode(), exception.getStatusCode());
    }

    @Test
    public void testUpdateTrip() {
        // given
        Long tripId = 1L;
        TripUpdateRequest request = new TripUpdateRequest(1L, "Updated Trip", LocalDateTime.now(), LocalDateTime.now().plusDays(3), false);
        Trip trip = new Trip();
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));

        // when
        assertDoesNotThrow(() -> tripService.updateTrip(tripId, request, userDetails));

        // then
        assertEquals("Updated Trip", trip.getName());
        assertEquals(LocalDate.now(), trip.getStartTime().toLocalDate());
        assertEquals(LocalDate.now().plusDays(3), trip.getEndTime().toLocalDate());
        assertFalse(trip.getOverseas());
    }

    @Test
    public void testUpdateTrip_NotFound() {
        // given
        Long tripId = 1L;
        TripUpdateRequest request = new TripUpdateRequest(tripId,"Updated Trip", LocalDateTime.now(), LocalDateTime.now().plusDays(3), false);
        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        // when
        TripUpdateException exception = assertThrows(TripUpdateException.class, () -> tripService.updateTrip(tripId, request, userDetails));

        // then
        assertEquals(TripUpdateErrorCode.TRIP_NOT_FOUND.getCode(), exception.getStatusCode());
    }

    @Test
    public void testDelete() {
        // given
        Long tripId = 1L;

        // when
        assertDoesNotThrow(() -> tripService.delete(tripId, userDetails));

        // then
        verify(tripRepository, times(1)).deleteById(tripId);
    }

    @Test
    public void testTripCountComparison() {
        // given
        when(tripRepository.countByOverseasFalse()).thenReturn(5L);
        when(tripRepository.countByOverseasTrue()).thenReturn(3L);

        // when
        TripRankResponse response = tripService.TripCountComparison();

        // then
        assertNotNull(response);
        assertEquals(2, response.rank().size());
        assertEquals("국내 여행", response.rank().get(0));
        assertEquals("해외 여행", response.rank().get(1));
    }
}

