package net.msnap.apiservice.v1.controller;

import net.msnap.apiservice.service.RandomMessageService;
import net.msnap.apiservice.v1.dto.PingResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1")
public class PingController {

    private final RandomMessageService randomMessageService;

    public PingController(RandomMessageService randomMessageService) {
        this.randomMessageService = randomMessageService;
    }

    @GetMapping("/ping")
    public PingResponse ping() {
        return new PingResponse(randomMessageService.getRandomPingMessage(), Instant.now());
    }

}
