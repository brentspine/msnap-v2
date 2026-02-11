package net.msnap.apiservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class OpenApiGeneratorTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void generateOpenApiDocs() throws Exception {
        String apiDocs = mockMvc.perform(get("/v1/api-docs"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Adjust the path to match your project structure
        // This assumes api-service and contracts are in the same parent directory
        Path outputPath = Paths.get("../contracts/src/main/java/net/msnap/contracts/openapi/api.json");

        // Ensure parent directories exist
        Files.createDirectories(outputPath.getParent());

        if (!apiDocs.contains("capes")) {
            throw new RuntimeException("Generated API docs do not contain expected 'capes' endpoint or schema.");
        }

        Files.writeString(outputPath, apiDocs);
        System.out.println("Generated OpenAPI specs to: " + outputPath.toAbsolutePath());
    }
}



