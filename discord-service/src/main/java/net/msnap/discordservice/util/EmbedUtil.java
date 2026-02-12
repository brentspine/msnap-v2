package net.msnap.discordservice.util;

import net.dv8tion.jda.api.EmbedBuilder;

public class EmbedUtil {

    public static EmbedBuilder setBrandedFooter(EmbedBuilder embed) {
        return embed.setFooter("MSnap", "https://cdn.discordapp.com/emojis/1471378658009677947.webp?size=32&animated=true");
    }

}
