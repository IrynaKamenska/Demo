package de.neuefische.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ServiceTest {
    private static MockWebServer mockWebServer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private BookService bookService;


//    @BeforeEach
//    void initialize() {
//        String baseUrl = String.format("http://localhost:%s",
//                mockWebServer.getPort());
//        bookService = new BookService(baseUrl);
//    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

@Test
    void getApiBookByIsbn_returnOneBook() {
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
        // when
//    BookResponseElement responseElement =
}


}
