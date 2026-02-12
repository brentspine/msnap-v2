package net.msnap.discordservice;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.msnap.discordservice.commands.CapeCommand;
import net.msnap.discordservice.commands.PingCommand;
import net.msnap.discordservice.commands.InviteCommand;
import net.msnap.discordservice.commands.EmojiListCommand;
import net.msnap.discordservice.service.ApiService;
import net.msnap.discordservice.service.CapeApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSnapDiscordBot {

    private static final Logger log = LoggerFactory.getLogger(MSnapDiscordBot.class);

    private JDA jda;
    public static ApiService apiService;
    public static CapeApiService capeApiService;

    public MSnapDiscordBot(ApiService apiService, CapeApiService capeApiService) throws InterruptedException {
        MSnapDiscordBot.apiService = apiService;
        MSnapDiscordBot.capeApiService = capeApiService;

        // In tests we don't want to boot a real Discord bot or hard-fail the JVM.
        // Spring sets this property automatically for @SpringBootTest.
        if (Boolean.parseBoolean(System.getProperty("spring.test.context", "false"))) {
            log.info("spring.test.context=true detected; skipping Discord bot startup");
            return;
        }

        // Load env - try .env.local first, then fall back to .env
        Dotenv dotenv = Dotenv.configure()
                .filename(".env.local")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        String token = dotenv.get("MSNAP_DISCORD_BOT_TOKEN");

        // If token not found in .env.local, try .env
        if (token == null || token.isEmpty()) {
            log.info("Token not found in .env.local. Trying .env");
            dotenv = Dotenv.configure()
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();
            token = dotenv.get("MSNAP_DISCORD_BOT_TOKEN");
        }
        if (token == null || token.isEmpty()) {
            log.error("MSNAP_DISCORD_BOT_TOKEN is not set in the environment variables.");
            throw new IllegalStateException("MSNAP_DISCORD_BOT_TOKEN is not configured");
        }

        try {
            jda = JDABuilder.create(token,
                        List.of(
                                GatewayIntent.MESSAGE_CONTENT,
                                GatewayIntent.GUILD_MEMBERS,
                                GatewayIntent.GUILD_MESSAGES,
                                GatewayIntent.GUILD_PRESENCES)
                        )
                    .disableCache(
                            CacheFlag.ACTIVITY,
                            CacheFlag.VOICE_STATE,
                            //CacheFlag.EMOJI,
                            CacheFlag.STICKER,
                            CacheFlag.CLIENT_STATUS,
                            CacheFlag.ONLINE_STATUS,
                            CacheFlag.SCHEDULED_EVENTS)
                    .setActivity(Activity.of(Activity.ActivityType.PLAYING, "\uD83E\uDEF0 Snap, and it's gone"))
                    .setChunkingFilter(ChunkingFilter.ALL).setMemberCachePolicy(MemberCachePolicy.ALL)
                    .setStatus(OnlineStatus.ONLINE)
                    .addEventListeners(
                            new PingCommand(),
                            new CapeCommand(),
                            new InviteCommand(),
                            new EmojiListCommand()
                    )
                    .build();
        } catch (InvalidTokenException e) {
            log.error("Bot could not be initialized", e);
            throw e;
        }
        jda.awaitReady();

        // Register slash commands
        jda.updateCommands().addCommands(
                Commands.slash("ping", "Check the bot's latency"),
                Commands.slash("cape", "Manage capes")
                        .addSubcommands(
                                new net.dv8tion.jda.api.interactions.commands.build.SubcommandData("list", "List all available capes")
                        ),
                Commands.slash("invite", "Get an invite link for this bot (admin only)"),
                Commands.slash("emojis", "List all custom emojis in this server (admin only)")
        ).queue();


        jda.getTextChannelById(1470902159179776061L).sendMessage("Bot started!").queue();
    }

}
