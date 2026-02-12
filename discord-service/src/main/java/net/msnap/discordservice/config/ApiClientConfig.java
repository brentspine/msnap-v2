package net.msnap.discordservice.config;

import net.msnap.apiclient.api.CapesApi;
import net.msnap.apiclient.api.PingControllerApi;
import net.msnap.apiclient.invoker.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the API Client.
 * Configures the ApiClient and exposes the generated API classes as Spring Beans.
 */
@Configuration
public class ApiClientConfig {

    @Value("${api.service.base-url:http://localhost:8080}")
    private String apiBaseUrl;

    /**
     * Creates and configures the ApiClient instance.
     *
     * @return the configured ApiClient
     */
    @Bean
    public ApiClient apiClient() {
        ApiClient apiClient = new ApiClient();
        apiClient.updateBaseUri(apiBaseUrl);
        return apiClient;
    }

    /**
     * Creates the PingControllerApi bean using the configured ApiClient.
     *
     * @param apiClient the ApiClient to use
     * @return the PingControllerApi instance
     */
    @Bean
    public PingControllerApi pingControllerApi(ApiClient apiClient) {
        return new PingControllerApi(apiClient);
    }

    /**
     * Creates the CapeControllerApi bean using the configured ApiClient.
     *
     * @param apiClient the ApiClient to use
     * @return the CapeControllerApi instance
     */
    @Bean
    public CapesApi capeControllerApi(ApiClient apiClient) {
        return new CapesApi(apiClient);
    }
}

