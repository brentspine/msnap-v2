package net.msnap.apiservice.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class RandomMessageService {

    private static final List<String> PING_RESPONSES = List.of(
            "Up and running",
            "Awaiting your commands",
            "Ready to serve",
            "All systems operational",
            "At your service"
    );

    private final Random random = new Random();

    public String getRandomPingMessage() {
        return PING_RESPONSES.get(random.nextInt(PING_RESPONSES.size()));
    }

}
