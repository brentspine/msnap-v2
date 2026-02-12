package net.msnap.discordservice.commands;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.msnap.apiclient.model.ListCapesDTO;
import net.msnap.discordservice.MSnapDiscordBot;
import net.msnap.discordservice.util.EmbedUtil;
import net.msnap.discordservice.util.EmojiConfig;

import java.awt.*;
import java.util.List;

@Slf4j
public class CapeCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("cape")) {
            return;
        }

        String subcommand = event.getSubcommandName();
        if (subcommand == null) {
            return;
        }

        if (subcommand.equals("list")) {
            handleListSubcommand(event);
        }
    }

    private void handleListSubcommand(SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        User user = event.getUser();
        List<ListCapesDTO> capes = MSnapDiscordBot.capeApiService.getCapes();

        if (capes.isEmpty()) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                    .setTitle("Available Capes")
                    .setDescription("No capes available or failed to fetch capes from API.")
                    .setColor(Color.RED);
            event.getHook().editOriginalEmbeds(embed.build()).queue();
            log.debug("No capes found for /cape list command from user {}", user.getName());
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Listing all " + capes.size() + " capes")
                .setColor(Color.CYAN);
        EmbedUtil.setBrandedFooter(embed);

        for (ListCapesDTO cape : capes) {
            String emoji = EmojiConfig.assureEmoji(cape.getDiscordEmoji());
            embed.appendDescription(emoji);
            embed.appendDescription(" " + cape.getDisplayName());
            // Because getAllowsCodes() could be null
            if (Boolean.TRUE.equals(cape.getAllowsCodes())) {
                embed.appendDescription(" " + EmojiConfig.ALLOWS_CAPE_CODES_EMOJI);
            }
            embed.appendDescription("\n");
        }

        /*if (!capes.isEmpty() && capes.get(0).getImage() != null) {
            embed.setThumbnail(capes.get(0).getImage());
        }*/

        event.getHook().editOriginalEmbeds(embed.build()).queue();
        log.debug("Responded to /cape list command from user {} with {} capes", user.getName(), capes.size());
    }

}
