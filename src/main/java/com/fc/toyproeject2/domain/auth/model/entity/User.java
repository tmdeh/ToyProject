package com.fc.toyproeject2.domain.auth.model.entity;

import com.fc.toyproeject2.domain.itinerary.model.entity.Stay;
import com.fc.toyproeject2.domain.like.model.entity.TripLike;
import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import com.fc.toyproeject2.global.model.entity.TimeStamp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@ToString
@Entity
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class User extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String pw;

    @Column
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Trip> tripList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<TripLike> tripLikes;

}
