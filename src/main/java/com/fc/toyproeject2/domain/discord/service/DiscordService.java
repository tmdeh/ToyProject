package com.fc.toyproeject2.domain.discord.service;

import com.fc.toyproeject2.domain.itinerary.model.entity.BaseCamp;
import com.fc.toyproeject2.domain.itinerary.model.entity.Move;
import com.fc.toyproeject2.domain.itinerary.model.entity.Stay;
import com.fc.toyproeject2.domain.trip.model.entity.Trip;
import com.fc.toyproeject2.domain.trip.repository.TripRepository;
import com.fc.toyproeject2.global.exception.error.DiscordErrorCode;
import com.fc.toyproeject2.global.exception.type.DiscordException;
import com.fc.toyproeject2.global.util.DiscordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiscordService {

    private final DiscordUtil discordUtil;
    private final TripRepository tripRepository;

    @Transactional
    public void discord(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new DiscordException(DiscordErrorCode.NOT_FOUND_TRIP_ID));

        discordUtil.sendMessage(getDiscordMessage(trip));
    }

    private String getDiscordMessage(Trip trip) {
        String message = "## --------------------------------------";
        message += "\n## 여행 \n";

        message += "```";
        message += "여행 시간 : " + trip.getStartTime();
        message += "\n여행 끝나는 시간 : " + trip.getEndTime();
        message += "\n해외 유무 : " + (trip.getOverseas() ? "O" : "X");
        message += "``` \n";

        message += "## 숙박 \n";
        for (BaseCamp baseCamp : trip.getBaseCampList()) {
            message += "```";
            message += "숙박 이름 : " + baseCamp.getName() + "\n";
            message += "체크인 : " + baseCamp.getCheckIn() + "\n";
            message += "체크아웃 : " + baseCamp.getCheckOut() + "\n";
            message += "``` \n";
        }

        message += "## 이동 \n";
        for (Move move : trip.getMoveList()) {
            message += "```";
            message += "이동 수단 : " + move.getMoveType().getName() + "\n";
            message += "출발 장소 : " + move.getStartPlace() + "\n";
            message += "도착 장소 : " + move.getEndPlace() + "\n";
            message += "출발 시간 : " + move.getStartTime() + "\n";
            message += "도착 시간 : " + move.getEndTime() + "\n";
            message += "``` \n";
        }

        message += "## 체류 \n";
        for (Stay stay : trip.getStayList()) {
            message += "```";
            message += "체류 할 장소 이름 : " + stay.getName() + "\n";
            message += "체류 시작 시간 : " + stay.getStartTime() + "\n";
            message += "체류 끝나는 시간 : " + stay.getEndTime() + "\n";
            message += "```\n";
        }

        message += "## --------------------------------------";
        return message;
    }

}
