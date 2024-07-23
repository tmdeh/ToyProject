package com.fc.toyproeject2.domain.comment.model.entity;

import com.fc.toyproeject2.domain.auth.model.entity.User;
import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import com.fc.toyproeject2.global.model.entity.TimeStamp;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    @Setter
    private String content;

    @Column
    @Setter
    private LocalDateTime createdAt;

}