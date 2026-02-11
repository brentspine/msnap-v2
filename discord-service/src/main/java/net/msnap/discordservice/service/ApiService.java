package net.msnap.discordservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.msnap.apiclient.api.PingControllerApi;
import net.msnap.apiclient.invoker.ApiException;
import net.msnap.apiclient.model.PingResponse;
import org.springframework.stereotype.Service;

/**
 * Example service that demonstrates how to use the API Client.
 * This service can be injected into your Discord commands or other components.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ApiService {

    private final PingControllerApi pingControllerApi;

    /**
     * Calls the ping endpoint of the API service.
     *
     * @return the ping response message, or an error message if the call fails
     */
    public String ping() {
        try {
            PingResponse response = pingControllerApi.ping();
            log.info("Ping successful: {}", response);
            return response.getMessage();
        } catch (ApiException e) {
            log.error("Failed to call ping API: {} - {}", e.getCode(), e.getMessage());
            return "Error: Could not reach the API service.";
        }
    }
}

