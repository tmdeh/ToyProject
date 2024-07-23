package com.fc.toyproeject2.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DiscordConfig {

    @Value("${discord.token}")
    private String token;

    @Bean
    public JDA connectionDiscord() {
        try {
            JDA jda = JDABuilder.createDefault(token)
                    .setStatus(OnlineStatus.ONLINE)
                    .setActivity(Activity.playing("여행 알림 On"))
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                    .build();

            return jda;
        } catch (Exception e) {
            throw e;
        }
    }

}
