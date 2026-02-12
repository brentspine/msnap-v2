package net.msnap.discordservice.util;

public class EmojiConfig {

    public static final String FALLBACK_EMOJI = ":question:";
    public static final String ALLOWS_CAPE_CODES_EMOJI = "<:allowscapecodes:1471372406995161183>";

    public static String assureEmoji(String emoji) {
        if (emoji == null || emoji.isBlank()) {
            return FALLBACK_EMOJI;
        }
        return emoji;
    }

}
