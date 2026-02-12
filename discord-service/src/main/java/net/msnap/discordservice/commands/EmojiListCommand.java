package net.msnap.discordservice.commands;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.msnap.discordservice.util.PermissionUtil;

import java.util.Comparator;
import java.util.List;

@Slf4j
public class EmojiListCommand extends ListenerAdapter {

    private static final int DISCORD_MESSAGE_LIMIT = 2000;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!"emojis".equals(event.getName())) {
            return;
        }

        Member member = event.getMember();
        if (!PermissionUtil.isAdmin(member)) {
            event.reply("You don't have permission to use this command.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        Guild guild = event.getGuild();
        if (guild == null) {
            event.reply("This command can only be used in a server.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        List<RichCustomEmoji> emojis = guild.getEmojis();

        if (emojis.isEmpty()) {
            event.reply("This server has no custom emojis.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        // Build response with preview + raw mention (<:name:id> / <a:name:id>)
        // Keep it under Discord's 2000-char limit by splitting into multiple ephemeral messages via followups.
        event.deferReply(true).queue();

        StringBuilder chunk = new StringBuilder("**Custom emojis in " + guild.getName() + "**\n");
        int sent = 0;

        for (RichCustomEmoji emoji : emojis) {
            String mention = toMention(emoji);
            // Preview is the mention itself. We also show the raw mention in backticks for easy copy.
            String line = mention + " `" + mention + "`\n";

            if (chunk.length() + line.length() > DISCORD_MESSAGE_LIMIT - 50) {
                event.getHook().sendMessage(chunk.toString()).setEphemeral(true).queue();
                sent++;
                chunk = new StringBuilder();
            }
            chunk.append(line);
        }

        if (!chunk.isEmpty()) {
            event.getHook().sendMessage(chunk.toString()).setEphemeral(true).queue();
            sent++;
        }

        // Remove the deferred "thinking" message (we use followups instead).
        event.getHook().deleteOriginal().queue();
        log.debug("Listed {} emojis in {} chunk(s) for admin {}", emojis.size(), sent, event.getUser().getName());
    }

    private static String toMention(RichCustomEmoji emoji) {
        // Both formats are acceptable "escaped mentions".
        // Animated emojis use <a:name:id>
        return "<" + (emoji.isAnimated() ? "a" : "") + ":" + emoji.getName() + ":" + emoji.getId() + ">";
    }
}

