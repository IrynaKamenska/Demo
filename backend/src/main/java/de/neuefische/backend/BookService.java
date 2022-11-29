package de.neuefische.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    private final String apiKey;
    private final WebClient webClient;

    public BookService(BookRepository bookRepository, @Value("${api.url}") String apiUrl, @Value("${api.key}") String apiKey) {
        this.bookRepository = bookRepository;
        this.apiKey = apiKey;
        this.webClient = WebClient.create(apiUrl);
    }


    String maxResults = "&maxResults=10";

//    "https://www.googleapis.com/books/v1/volumes?q=javascript&key=AIzaSyBp5xzV9nlcvQsRKCVm09RAaprMA9H2Q9Q&maxResults=10"


    //  https://www.googleapis.com/books/v1/volumes?q=9783756271290&key=AIzaSyBp5xzV9nlcvQsRKCVm09RAaprMA9H2Q9Q
    //    private final WebClient webClient = WebClient.create();


    // Todo: 3 unittest: findet, nicht findet leere array, ungültige isbn
    public List<Book> getApiBookByIsbn(String isbn) throws ResponseStatusException {
        ResponseEntity<BookResponseElement> bookResponse = webClient
                .get()
                .uri("?q=isbn:" + isbn + "&key=" + apiKey)
//                .uri("https://www.googleapis.com/books/v1/volumes?q=javascript&key=AIzaSyBp5xzV9nlcvQsRKCVm09RAaprMA9H2Q9Q&maxResults=10")
                .retrieve()
                .toEntity(BookResponseElement.class)
                .block();
        System.out.println("BOOK Response" + bookResponse);

        return Optional.ofNullable(bookResponse)
                .map(HttpEntity::getBody)
                .map(BookResponseElement::bookItems)
                .map(bookList -> bookList.stream()
                        .filter(book -> book.volumeInfo()
                                .industryIdentifiers()
                                .stream()
                                .anyMatch(isbn1 -> isbn1.identifier()
                                        .equals(isbn)))
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new NoBookFoundException("No books could be found"));

    }

    public Book saveBookInDB(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getAllApiBooks(String searchText) {
        ResponseEntity<BookResponseElement> bookResponse = webClient
                .get()
                .uri("?q="+searchText+"&key="+apiKey+maxResults)
                .retrieve()
                .toEntity(BookResponseElement.class)
                .block();

        System.out.println("BOOK Response" + bookResponse);

        return Optional.ofNullable(bookResponse)
                .map(HttpEntity::getBody)
                .map(BookResponseElement::bookItems)
                .map(bookList -> bookList.stream()
                .collect(Collectors.toList()))
                .orElseThrow(() -> new NoBookFoundException("No books could be found"));



      /*  BookResponseElement responseBody;
        System.out.println("BOOK Response" + bookResponse);

        if (bookResponse != null) {
            responseBody = bookResponse.getBody();
            System.out.println(" Response BODY" + responseBody);
        } else {
            throw new BookResponseException("Book Response is null");
        }
        if (responseBody != null) {
            return responseBody.bookItems();
        } else throw new BookResponseException("Response body is null");*/
    }

//    Isbn isbn = new Isbn("type", "identifier");
//    ImageLinks imageLinks = new ImageLinks("link1");
//    VolumeInfo volumeInfo = new VolumeInfo("title", List.of("Author1"), List.of(isbn), imageLinks);
//    Book book = new Book("id1", volumeInfo);
//    String findIsbn = book.volumeInfo().industryIdentifiers().get(0).identifier();


/*    public void saveApiBooksToDB() {
        ResponseEntity<BookResponseElement> bookResponse = webClient
                .get()
                .uri("https://www.googleapis.com/books/v1/volumes?q=javascript&key=AIzaSyBp5xzV9nlcvQsRKCVm09RAaprMA9H2Q9Q&maxResults=10")
                .retrieve()
                .toEntity(BookResponseElement.class)
                .block();
        BookResponseElement responseBody;
        System.out.println("BOOK Response" + bookResponse);

        if (bookResponse != null) {
            responseBody = bookResponse.getBody();
            System.out.println(" Response BODY" + responseBody);
        } else {
            throw new BookResponseException("Book Response is null");
        }
        if (responseBody != null) {
            responseBody.bookItems();
        } else throw new BookResponseException("Response body is null");
        List<Book> bookList = responseBody.bookItems();
        for (Book book : bookList) {
            Book bookToInsert = new Book(book.id(), book.volumeInfo());

            if (!bookRepository.existsById(book.id())) {
                bookRepository.insert(bookToInsert);
            }
        }

    }*/

    /*    public void saveBookWithIsbnAndBodyInDB(String isbn, Book bookToSave) {
        bookToSave = getApiBookByIsbn(isbn);
        if (!bookRepository.existsById(bookToSave.id())) {
            bookRepository.insert(bookToSave);
        }
    }*/



}
