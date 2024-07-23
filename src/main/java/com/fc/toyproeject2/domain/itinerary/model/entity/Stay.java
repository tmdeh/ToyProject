package com.fc.toyproeject2.domain.itinerary.model.entity;

import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import com.fc.toyproeject2.global.model.entity.TimeStamp;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Stay extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Trip trip;

    @Column
    private String name;

    @Column
    private String address;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    public void update(String name, String address, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.address = address;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
