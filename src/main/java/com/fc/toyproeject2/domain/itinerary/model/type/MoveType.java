package com.fc.toyproeject2.domain.itinerary.model.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MoveType {
    MOVE("이동 수단"),
    CAR("차"),
    HORSE("말"),
    BIKE("오토바이"),
    AIRPLANE("비행기"),
    BOAT("배"),
    BICYCLE("자전거"),
    WOLF("늑대"),
    RHYTHM("리듬"),
    OTHER("기타");

    private final String name;
}
