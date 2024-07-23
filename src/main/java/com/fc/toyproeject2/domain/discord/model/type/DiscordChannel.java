package com.fc.toyproeject2.domain.discord.model.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DiscordChannel {
    ALRAM_CHANNEL("여행알림")
    ;

    private final String channelName;
}
