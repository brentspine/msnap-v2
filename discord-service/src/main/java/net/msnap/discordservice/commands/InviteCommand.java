package net.msnap.discordservice.commands;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.msnap.discordservice.util.PermissionUtil;

@Slf4j
public class InviteCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!"invite".equals(event.getName())) {
            return;
        }

        if (!PermissionUtil.isAdmin(event.getMember())) {
            event.reply("You don't have permission to use this command.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        // This is the standard OAuth2 invite URL template.
        // We use the current application's client ID.
        String clientId = event.getJDA().getSelfUser().getApplicationId();
        String inviteUrl = "https://discord.com/api/oauth2/authorize?client_id=" + clientId +
                "&permissions=0&scope=bot%20applications.commands";

        event.reply("Invite link: " + inviteUrl)
                .setEphemeral(true)
                .queue();
    }
}

