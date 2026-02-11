package net.msnap.apiservice.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public record PingResponse(
        @Schema(example = "pong")
        String message,

        @Schema(description = "Zeitpunkt, an dem der Server den Ping verarbeitet hat", example = "2026-02-11T10:15:30Z")
        Instant receivedAt
) {}