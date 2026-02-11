package net.msnap.discordservice.commands;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.msnap.discordservice.MSnapDiscordBot;
import net.msnap.discordservice.service.ApiService;

import java.awt.*;

@Slf4j
public class PingCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("ping")) {
            return;
        }

        long gatewayLatency = event.getJDA().getGatewayPing();
        long restLatency = System.currentTimeMillis();
        String pingMessage = MSnapDiscordBot.apiService.ping();
        restLatency = System.currentTimeMillis() - restLatency;
        User user = event.getUser();
        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                .setDescription(":heartpulse: Gateway Latency: **" + gatewayLatency + "** ms\n" +
                        ":stopwatch: Rest Latency: **" + restLatency + "** ms\n" +
                        ":envelope_with_arrow: Message: **" + pingMessage + "**")
                .setColor(Color.DARK_GRAY);

        event.replyEmbeds(embed.build()).queue();
        log.debug("Responded to /ping command from user {}", user.getName());
    }
}



