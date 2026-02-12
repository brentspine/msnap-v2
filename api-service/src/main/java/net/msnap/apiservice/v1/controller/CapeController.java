package net.msnap.apiservice.v1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.msnap.apiservice.service.CapeService;
import net.msnap.apiservice.v1.dto.capes.ListCapesDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/capes")
@Tag(name = "Capes", description = "Endpoints for managing and retrieving capes")
public class CapeController {

    private final CapeService capeService;

    public CapeController(CapeService capeService) {
        this.capeService = capeService;
    }

    @GetMapping
    @Operation(summary = "List all capes", description = "Returns a list of all available capes")
    public List<ListCapesDTO> listCapes() {
        return capeService.getAllCapes();
    }
}

