package net.msnap.apiservice.service;

import net.msnap.apiservice.persistence.entity.Cape;
import net.msnap.apiservice.persistence.repository.CapeRepository;
import net.msnap.apiservice.v1.dto.capes.ListCapesDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CapeService {

    private final CapeRepository capeRepository;

    public CapeService(CapeRepository capeRepository) {
        this.capeRepository = capeRepository;
    }

    @Transactional(readOnly = true)
    public List<ListCapesDTO> getAllCapes() {
        return capeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ListCapesDTO mapToDTO(Cape cape) {
        return new ListCapesDTO(
                cape.getAlias(),
                cape.getDisplayName(),
                cape.getDescription(),
                cape.getImage(),
                cape.getDiscordEmoji(),
                cape.getUpdatedAt(),
                cape.isAllowsCodes()
        );
    }
}
