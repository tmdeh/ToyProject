package com.fc.toyproeject2.global.util;

import com.fc.toyproeject2.domain.discord.model.type.DiscordChannel;
import com.fc.toyproeject2.global.exception.error.DiscordErrorCode;
import com.fc.toyproeject2.global.exception.type.DiscordException;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@RequiredArgsConstructor
public class DiscordUtil {

    private final JDA jda;

    public void sendMessage(String context) {
        String channelName = DiscordChannel.ALRAM_CHANNEL.getChannelName();

        for (Guild g : jda.getGuilds()) {
            TextChannel textChannel = g.getTextChannelsByName(channelName, false).stream().findFirst().orElse(null);

            if (textChannel == null)
                throw new DiscordException(DiscordErrorCode.NOT_FOUND_DISCORD_CHANNEL);

            textChannel.sendMessage(MessageCreateData.fromContent(context)).queue();
        }


    }

}
