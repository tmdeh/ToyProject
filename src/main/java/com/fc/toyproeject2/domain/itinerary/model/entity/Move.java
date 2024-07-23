package com.fc.toyproeject2.domain.itinerary.model.entity;

import com.fc.toyproeject2.domain.itinerary.model.type.MoveType;
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
public class Move extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Trip trip;

    @Enumerated
    private MoveType moveType;

    @Column
    private String startPlace;

    @Column
    private String startAddress;

    @Column
    private String endPlace;

    @Column
    private String endAddress;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    public void update(MoveType moveType, String startPlace, String startAddress, String endPlace, String endAddress, LocalDateTime startTime, LocalDateTime endTime) {
        this.moveType = moveType;
        this.startPlace = startPlace;
        this.startAddress = startAddress;
        this.endPlace = endPlace;
        this.endAddress = endAddress;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
