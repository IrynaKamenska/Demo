package de.neuefische.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.neuefische.backend.model.*;
import de.neuefische.backend.repository.BookRepository;
import de.neuefische.backend.service.BookService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;


import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ServiceBookTest {
    private static MockWebServer mockWebServer;
    private final BookRepository bookRepository = mock(BookRepository.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private BookService bookService;

    @Value("${api.key}")
    private String apiKey;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s",
                mockWebServer.getPort());
        bookService = new BookService(bookRepository, baseUrl, apiKey);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
    @Test
    void getAllBooks_ifNoBooksExist_returnEmptyList() {
        //given
        List<Book> books = new ArrayList<>();
        when(bookRepository.findAll()).thenReturn(books);

        //when
        List<Book> actual = bookService.getAllBooks();

        //then
        verify(bookRepository).findAll();
        assertEquals(books, actual);

    }

    @Test
    void getAllBooks_returnListWithOneBook() {
        //given
        String previewLink = "http://books.google.de/books/preview";
        Isbn isbn_13 = new Isbn("ISBN_13", "9783897214484");
        Isbn isbn_10 = new Isbn("ISBN_10", "3897214482");
        ImageLinks thumbnail = new ImageLinks("http://books.google.com/books/thumbnail");
        VolumeInfo volumeInfo = new VolumeInfo("Java von Kopf bis Fuß", List.of("Kathy Sierra", "Bert Bates"), List.of(isbn_13, isbn_10), thumbnail, previewLink);
        Book book = new Book("5eDWcLzdAcYC", volumeInfo, BookState.AVAILABLE);


        Book foundBook = book.withVolumeInfo(volumeInfo.withTitle("Java von Kopf bis Fuß").withAuthors(List.of("Kathy Sierra", "Bert Bates")).withIndustryIdentifiers(List.of(isbn_13, isbn_10))
                .withImageLinks(thumbnail).withPreviewLink(previewLink));


        when(bookRepository.findAll()).thenReturn(List.of(foundBook));

        //when
        List<Book> expected = List.of(foundBook);
        List<Book> actual = bookService.getAllBooks();

        //then
        verify(bookRepository).findAll();
        assertEquals(expected, actual);

    }

    @Test
    void addNewBookWithoutId_returnBookWithId() {
        //given
        String previewLink = "http://books.google.de/books/preview";
        Isbn isbn_13 = new Isbn("ISBN_13", "9783897214484");
        Isbn isbn_10 = new Isbn("ISBN_10", "3897214482");
        ImageLinks thumbnail = new ImageLinks("http://books.google.com/books/thumbnail");
        VolumeInfo volumeInfo = new VolumeInfo("Java von Kopf bis Fuß", List.of("Kathy Sierra", "Bert Bates"), List.of(isbn_13, isbn_10), thumbnail, previewLink);
        Book book = new Book(null, volumeInfo, BookState.AVAILABLE);

        Book saveBook = book.withVolumeInfo(volumeInfo.withTitle("Java von Kopf bis Fuß").withAuthors(List.of("Kathy Sierra", "Bert Bates")).withIndustryIdentifiers(List.of(isbn_13, isbn_10))
                .withImageLinks(thumbnail).withPreviewLink(previewLink));
        when(bookRepository.save(saveBook)).thenReturn(saveBook.withId("id1"));

        //when
        Book actual = bookService.saveBookInDB(saveBook);
        Book expected = saveBook.withId("id1");

        //then
        assertEquals(expected, actual);
    }


    @Test
    void addNewBookWithId_returnBook() {
        //given
        String previewLink = "http://books.google.de/books/preview";
        Isbn isbn_13 = new Isbn("ISBN_13", "9783897214484");
        Isbn isbn_10 = new Isbn("ISBN_10", "3897214482");
        ImageLinks thumbnail = new ImageLinks("http://books.google.com/books/thumbnail");
        VolumeInfo volumeInfo = new VolumeInfo("Java von Kopf bis Fuß", List.of("Kathy Sierra", "Bert Bates"), List.of(isbn_13, isbn_10), thumbnail, previewLink);
        Book book = new Book("id1", volumeInfo, BookState.AVAILABLE);

        Book saveBook = book.withId("id1").withVolumeInfo(volumeInfo.withTitle("Java von Kopf bis Fuß").withAuthors(List.of("Kathy Sierra", "Bert Bates")).withIndustryIdentifiers(List.of(isbn_13, isbn_10))
                .withImageLinks(thumbnail).withPreviewLink(previewLink));
        when(bookRepository.save(saveBook)).thenReturn(saveBook);

        //when
        Book actual = bookService.saveBookInDB(saveBook);

        //then
        assertEquals(saveBook, actual);
    }

    @Test
    void getApiBookByIsbn_returnOneBook() throws Exception {
        //given
        String previewLink = "http://books.google.de/books/preview";
        Isbn isbn_13 = new Isbn("ISBN_13", "9783897214484");
        Isbn isbn_10 = new Isbn("ISBN_10", "3897214482");
        ImageLinks thumbnail = new ImageLinks("http://books.google.com/books/thumbnail");
        VolumeInfo volumeInfo = new VolumeInfo("Java von Kopf bis Fuß", List.of("Kathy Sierra", "Bert Bates"), List.of(isbn_13, isbn_10), thumbnail, previewLink);

        BookResponseElement mockBokListResponse = new BookResponseElement(1,
                List.of(new Book("5eDWcLzdAcYC", volumeInfo, BookState.AVAILABLE)));

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockBokListResponse))
                .addHeader("Content-Type", "application/json")
        );
        // when
        BookResponseElement actual = bookService.getApiBookByIsbn("3897214482");
        BookResponseElement expected = mockBokListResponse;

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        //then
        assertEquals(expected, actual);
        assertEquals("GET", recordedRequest.getMethod());

    }


    @Test
    void getApiBookByInvalidIsbn_returnZeroBooks() throws Exception {
        //given
        BookResponseElement mockBokListResponse = new BookResponseElement(0, List.of());

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockBokListResponse))
                .addHeader("Content-Type", "application/json")
        );

        // when
        BookResponseElement actual = bookService.getApiBookByIsbn("abc");
        BookResponseElement expected = mockBokListResponse;

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        //then
        assertEquals(expected, actual);
        assertEquals("GET", recordedRequest.getMethod());

    }



    @Test
    void getApiBookByKeyWord_returnOneBook() throws Exception {
        //given
        String previewLink = "http://books.google.de/books/preview";
        Isbn isbn_13 = new Isbn("ISBN_13", "9783897214484");
        Isbn isbn_10 = new Isbn("ISBN_10", "3897214482");
        ImageLinks thumbnail = new ImageLinks("http://books.google.com/books/thumbnail");
        VolumeInfo volumeInfo = new VolumeInfo("Java von Kopf bis Fuß", List.of("Kathy Sierra", "Bert Bates"), List.of(isbn_13, isbn_10), thumbnail, previewLink);

        BookResponseElement mockBokListResponse = new BookResponseElement(1,
                List.of(new Book("5eDWcLzdAcYC", volumeInfo, BookState.AVAILABLE)));

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockBokListResponse))
                .addHeader("Content-Type", "application/json")
        );
        // when
        BookResponseElement actual = bookService.getAllApiBooks("Java");
        BookResponseElement expected = mockBokListResponse;

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        //then
        assertEquals(expected, actual);
        assertEquals("GET", recordedRequest.getMethod());

    }


    @Test
    void getApiBookByNotExistingKeyword_returnZeroBooks() throws Exception {
        //given
        BookResponseElement mockBokListResponse = new BookResponseElement(0, List.of());

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockBokListResponse))
                .addHeader("Content-Type", "application/json")
        );

        // when
        BookResponseElement actual = bookService.getAllApiBooks("12345dfjlkdfhdsjhfjkdsgfjkds");
        BookResponseElement expected = mockBokListResponse;

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        //then
        assertEquals(expected, actual);
        assertEquals("GET", recordedRequest.getMethod());

    }


}
