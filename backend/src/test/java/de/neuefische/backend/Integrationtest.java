package de.neuefische.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class Integrationtest {

    @Autowired
    private MockMvc mockMvc;
    private static MockWebServer mockWebServer;
    @Autowired
    private ObjectMapper objectMapper;


    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @DynamicPropertySource
    static void backendProperties(DynamicPropertyRegistry registry) {
        registry.add("api.url", () -> mockWebServer.url("/").toString());

    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }


    @Test
    void getAllBooksFromDB_returnEmptyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }


    @Test
    void searchApiBooksByKeyword_returnListOfBooks() throws Exception {
        //given
        String previewLink = "http://books.google.de/books/preview";
        Isbn isbn_13 = new Isbn("ISBN_13", "9783897214484");
        Isbn isbn_10 = new Isbn("ISBN_10", "3897214482");
        ImageLinks thumbnail = new ImageLinks("http://books.google.com/books/thumbnail");
        VolumeInfo volumeInfo = new VolumeInfo("Java von Kopf bis Fuß", List.of("Kathy Sierra", "Bert Bates"), List.of(isbn_13, isbn_10), thumbnail, previewLink);
        Book book = new Book("5eDWcLzdAcYC", volumeInfo);

        BookResponseElement mockBokListResponse = new BookResponseElement(List.of(book), 1);
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockBokListResponse))
                .addHeader("Content-Type", "application/json")
        );

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/search/java"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [ {
                                        "id": "5eDWcLzdAcYC",
                                        "volumeInfo": {
                                            "title": "Java von Kopf bis Fuß",
                                            "authors": [
                                                "Kathy Sierra",
                                                "Bert Bates"
                                            ],
                                            "industryIdentifiers": [
                                                {
                                                    "type": "ISBN_10",
                                                    "identifier": "3897214482"
                                                },
                                                {
                                                    "type": "ISBN_13",
                                                    "identifier": "9783897214484"
                                                }
                                            ],
                                            "imageLinks": {
                                                "thumbnail": "http://books.google.com/books/thumbnail"
                                            },
                                            "previewLink": "http://books.google.de/books/preview"
                                        }
                                    }]
                                """
                ));

    }

    @Test
    void serchApiBooksByEmptyString_return404() throws Exception {
        //given
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
        );

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/search/"))
                // then
                .andExpect(status().isNotFound());

    }


    @Test
    void searchApiBooksByIsbn_returnListWithOneBook() throws Exception {
        //given
        String previewLink = "http://books.google.de/books/preview";
        Isbn isbn_13 = new Isbn("ISBN_13", "9783897214484");
        Isbn isbn_10 = new Isbn("ISBN_10", "3897214482");
        ImageLinks thumbnail = new ImageLinks("http://books.google.com/books/thumbnail");
        VolumeInfo volumeInfo = new VolumeInfo("Java von Kopf bis Fuß", List.of("Kathy Sierra", "Bert Bates"), List.of(isbn_13, isbn_10), thumbnail, previewLink);
        Book book = new Book("5eDWcLzdAcYC", volumeInfo);

        BookResponseElement mockBokListResponse = new BookResponseElement(List.of(book), 1);

/*        String mockBookListResponse = """
                {
                                        "id": "5eDWcLzdAcYC",
                                        "volumeInfo": {
                                            "title": "Java von Kopf bis Fuß",
                                            "authors": [
                                                "Kathy Sierra",
                                                "Bert Bates"
                                            ],
                                            "industryIdentifiers": [
                                                {
                                                    "type": "ISBN_10",
                                                    "identifier": "3897214482"
                                                },
                                                {
                                                    "type": "ISBN_13",
                                                    "identifier": "9783897214484"
                                                }
                                            ],
                                            "imageLinks": {
                                                "thumbnail": "http://books.google.com/books/thumbnail"
                                            },
                                            "previewLink": "http://books.google.de/books/preview"
                                        }
                                    }
                """;*/
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockBokListResponse))
                .addHeader("Content-Type", "application/json")
        );

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/isbn/9783897214484"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [ {
                                        "id": "5eDWcLzdAcYC",
                                        "volumeInfo": {
                                            "title": "Java von Kopf bis Fuß",
                                            "authors": [
                                                "Kathy Sierra",
                                                "Bert Bates"
                                            ],
                                            "industryIdentifiers": [
                                                {
                                                    "type": "ISBN_10",
                                                    "identifier": "3897214482"
                                                },
                                                {
                                                    "type": "ISBN_13",
                                                    "identifier": "9783897214484"
                                                }
                                            ],
                                            "imageLinks": {
                                                "thumbnail": "http://books.google.com/books/thumbnail"
                                            },
                                            "previewLink": "http://books.google.de/books/preview"
                                        }
                                    }]
                                """
                ));

    }

    @Test
    void searchApiBooksByInvalidIsbn_returnEmptyList() throws Exception {

//        BookResponseElement mockBokListResponse = new BookResponseElement(List.of(), 1);
        String mockWebServerResponse = """
                        {
                          "kind": "books#volumes",
                          "totalItems": 0
                        }""";
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        {
                          "kind": "books#volumes",
                          "totalItems": 0
                        }""")
                .addHeader("Content-Type", "application/json")
        );

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/isbn/abc"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json(mockWebServerResponse));
    }




}

