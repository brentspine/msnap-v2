package net.msnap.discordservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.msnap.apiclient.api.CapesApi;
import net.msnap.apiclient.invoker.ApiException;
import net.msnap.apiclient.model.ListCapesDTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CapeApiService {

    private final CapesApi capesApi;

    public List<ListCapesDTO> getCapes() {
        try {
            return capesApi.listCapes();
        } catch (ApiException e) {
            log.error("Failed to fetch capes: {} - {}", e.getCode(), e.getMessage());
            return Collections.emptyList();
        }
    }
}

