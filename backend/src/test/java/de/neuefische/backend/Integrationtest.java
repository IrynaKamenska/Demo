package de.neuefische.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class Integrationtest {

    @Autowired
    private MockMvc mockMvc;
    private static MockWebServer mockWebServer;
    private ObjectMapper objectMapper;


    @BeforeAll

    @Test
    void testRecord() {
    }
}

