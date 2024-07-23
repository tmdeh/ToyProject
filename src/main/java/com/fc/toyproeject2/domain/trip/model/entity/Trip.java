package com.fc.toyproeject2.domain.trip.model.entity;

import com.fc.toyproeject2.domain.auth.model.entity.User;
import com.fc.toyproeject2.domain.comment.model.entity.Comment;
import com.fc.toyproeject2.domain.itinerary.model.entity.BaseCamp;
import com.fc.toyproeject2.domain.itinerary.model.entity.Move;
import com.fc.toyproeject2.domain.itinerary.model.entity.Stay;
import com.fc.toyproeject2.domain.like.model.entity.TripLike;
import com.fc.toyproeject2.global.model.entity.TimeStamp;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Trip extends TimeStamp {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Setter
    private String name;

    @Column
    @Setter
    private LocalDateTime startTime;

    @Column
    @Setter
    private LocalDateTime endTime;

    @Column
    @Setter
    private Boolean overseas;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip", cascade = CascadeType.REMOVE)
    private List<Move> moveList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip", cascade = CascadeType.REMOVE)
    private List<BaseCamp> baseCampList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip", cascade = CascadeType.REMOVE)
    private List<Stay> stayList;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip", cascade = CascadeType.ALL)
    private List<TripLike> tripLikes;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<Comment> comments;

}

