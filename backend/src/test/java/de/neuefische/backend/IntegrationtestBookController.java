package de.neuefische.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.neuefische.backend.model.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.List;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationtestBookController {

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
    @Order(2)
    @DirtiesContext
    void getAllBooksFromDB_returnEmptyList() throws Exception {
        Thread.sleep(100);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }


    @Test
    @DirtiesContext
    void addBookWithoutId_returnBook() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/books/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""                             
                                {
                                "volumeInfo":{"title":"Java von Kopf bis Fuß"
                                ,"authors":["Kathy Sierra","Bert Bates"],
                                "industryIdentifiers":[
                                {"type":"ISBN_13","identifier":"9783897214484"},
                                {"type":"ISBN_10","identifier":"3897214482"}
                                ],
                                "imageLinks":{"thumbnail":"http://books.google.com/books/thumbnail"},
                                "previewLink":"http://books.google.de/books/preview"},
                                "bookState": "AVAILABLE" }
                                """))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        Book book = objectMapper.readValue(body, Book.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{"id":"<id>","volumeInfo":{
                        "title":"Java von Kopf bis Fuß",
                        "authors":["Kathy Sierra","Bert Bates"],
                        "industryIdentifiers":[{"type":"ISBN_13","identifier":"9783897214484"},
                        {"type":"ISBN_10","identifier":"3897214482"}],
                        "imageLinks":{"thumbnail":"http://books.google.com/books/thumbnail"},
                        "previewLink":"http://books.google.de/books/preview"},
                        "bookState": "AVAILABLE"}]
                        """.replace("<id>", book.id())))
                .andExpect(jsonPath("$..title").isNotEmpty())
                .andExpect(jsonPath("$..volumeInfo").isNotEmpty())
                .andExpect(jsonPath("$..authors").isArray())
                .andExpect(jsonPath("$..industryIdentifiers").isArray())
                .andExpect(jsonPath("$..imageLinks").isNotEmpty())
                .andExpect(jsonPath("$..previewLink").isNotEmpty());

    }

    @Test
    @Order(4)
    @DirtiesContext
    void searchApiBooksByIsbn_returnListWithOneBook() throws Exception {
        //given
        Thread.sleep(100);
        String mockBookListResponse = """
                 {
                                "items":[
                                {"id":"5eDWcLzdAcYC",
                                "volumeInfo":{"title":"Java von Kopf bis Fuß"
                                ,"authors":["Kathy Sierra","Bert Bates"],
                                "industryIdentifiers":[
                                {"type":"ISBN_13","identifier":"9783897214484"},
                                {"type":"ISBN_10","identifier":"3897214482"}
                                ],
                                "imageLinks":{"thumbnail":"http://books.google.com/books/thumbnail"},
                                "previewLink":"http://books.google.de/books/preview"}}],
                                
                                "totalItems":1
                                }
                """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(mockBookListResponse)
                .addHeader("Content-Type", "application/json")
        );

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/isbn/9783897214484"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                {
                                "items":[
                                {"id":"5eDWcLzdAcYC",
                                "volumeInfo":{"title":"Java von Kopf bis Fuß"
                                ,"authors":["Kathy Sierra","Bert Bates"],
                                "industryIdentifiers":[
                                {"type":"ISBN_13","identifier":"9783897214484"},
                                {"type":"ISBN_10","identifier":"3897214482"}
                                ],
                                "imageLinks":{"thumbnail":"http://books.google.com/books/thumbnail"},
                                "previewLink":"http://books.google.de/books/preview"}}],
                                
                                "totalItems":1
                                }
                                """
                ));

    }

    @Test
    @Order(1)
    @DirtiesContext
    void searchApiBooksByInvalidIsbn_return0TotalItems() throws Exception {
        Thread.sleep(100);
        String mockBookListResponse = """
                 {
                     "items": [],
                     "totalItems": 0
                 }
                """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(mockBookListResponse)
                .addHeader("Content-Type", "application/json")
        );

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/isbn/abc"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                {
                                     "items": [],
                                     "totalItems": 0
                                 }
                                """
                ));
    }


    @Test
    void searchApiBooksByKeyword_returnListOfBooks() throws Exception {
        //given
        String previewLink = "http://books.google.de/books/preview";
        Isbn isbn_13 = new Isbn("ISBN_13", "9783897214484");
        Isbn isbn_10 = new Isbn("ISBN_10", "3897214482");
        ImageLinks thumbnail = new ImageLinks("http://books.google.com/books/thumbnail");
        VolumeInfo volumeInfo = new VolumeInfo("Java von Kopf bis Fuß", List.of("Kathy Sierra", "Bert Bates"), List.of(isbn_13, isbn_10), thumbnail, previewLink);
        Book book = new Book("5eDWcLzdAcYC", volumeInfo, BookState.AVAILABLE);

        BookResponseElement mockBokListResponse = new BookResponseElement(1, List.of(book));
        System.out.println("Response:" + mockBokListResponse);
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
                                {"items":[
                                {"id":"5eDWcLzdAcYC",
                                "volumeInfo":{
                                "title":"Java von Kopf bis Fuß",
                                "authors":["Kathy Sierra","Bert Bates"],
                                "industryIdentifiers":[{"type":"ISBN_13","identifier":"9783897214484"},{"type":"ISBN_10","identifier":"3897214482"}],
                                "imageLinks":{"thumbnail":"http://books.google.com/books/thumbnail"},
                                "previewLink":"http://books.google.de/books/preview"}}],
                                "totalItems":1}
                                """
                ));

    }



    @Test
    void searchApiBooksByKeyword_returnZeroTotalItems() throws Exception {
        //given
        String mockBookListResponse = """
                 {
                     "items": [],
                     "totalItems": 0
                 }
                """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(mockBookListResponse)
                .addHeader("Content-Type", "application/json")
        );

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/search/12345dfjlkdfhdsjhfjkdsgfjkds"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                {
                                     "items": [],
                                     "totalItems": 0
                                 }
                                """
                ));

    }
}

