package net.msnap.apiservice.v1.controller;

import net.msnap.apiservice.v1.dto.capes.ListCapesDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CapeController {

    @GetMapping("/capes")
    public List<ListCapesDTO> listCapes() {
        // In a real implementation, you would fetch this from a service or database
        return List.of(new ListCapesDTO(
            "15th_anniversary",
            "15th Anniversary Cape",
            "This cape was given to visitors of the 15 Year Anniversary page on Minecraft.net who selected the claim button.",
            "https://msnap.net/img/capes/15th_anniversary.png",
            Instant.now(),
            false
        ));
    }
}

