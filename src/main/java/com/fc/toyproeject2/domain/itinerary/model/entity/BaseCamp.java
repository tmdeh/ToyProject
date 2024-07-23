package com.fc.toyproeject2.domain.itinerary.model.entity;

import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import com.fc.toyproeject2.global.model.entity.TimeStamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class BaseCamp extends TimeStamp {

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
    private LocalDateTime checkIn;

    @Column
    private LocalDateTime checkOut;

    public void update(String name, String address, LocalDateTime checkIn, LocalDateTime checkOut) {
        this.name = name;
        this.address = address;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
}
