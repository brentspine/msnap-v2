package net.msnap.apiservice.v1.dto.capes;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public record ListCapesDTO(
        @Schema(description = "Unique internal, readable identifier for the cape", example = "15th_anniversary")
        String alias,
        @Schema(description = "Human readable name of the cape", example = "15th Anniversary Cape")
        String displayName,
        @Schema(description = "Description of the cape", example = "This cape was given to visitors of the 15 Year Anniversary page on Minecraft.net who selected the claim button.")
        String description,
        @Schema(description = "Full URL to the cape image")
        String image,
        @Schema(description = "Discord custom emoji mention text associated with this cape (raw), e.g. <:minecraftexperience:1471358363764916337> or <a:party:1471358363764916337>", example = "<:minecraftexperience:1471358363764916337>")
        String discordEmoji,
        @Schema(description = "Last update time of the cape")
        Instant updatedAt,
        @Schema(description = "Whether the cape allows codes to be redeemed for it. See CapeCodes section.")
        boolean allowsCodes
) {}
