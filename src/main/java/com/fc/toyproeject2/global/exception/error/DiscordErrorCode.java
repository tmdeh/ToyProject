package com.fc.toyproeject2.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
public enum DiscordErrorCode implements ErrorCode{
    NOT_FOUND_TRIP_ID(HttpStatusCode.valueOf(404), "Trip ID가 존재하지 않습니다."),
    NOT_FOUND_DISCORD_CHANNEL(HttpStatusCode.valueOf(404), "Discrod 의 해당 채널이 존재하지 않습니다.")
    ;
    private final HttpStatusCode code;
    private final String info;
}
