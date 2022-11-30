package de.neuefische.backend.service;

import de.neuefische.backend.repository.BookRepository;
import de.neuefische.backend.model.BookResponseElement;

import de.neuefische.backend.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


import static java.util.Objects.requireNonNull;

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

    String maxResults = "&maxResults=5";



    public BookResponseElement getApiBookByIsbn(String isbn) throws ResponseStatusException {
        ResponseEntity<BookResponseElement> bookResponse = requireNonNull(webClient
                .get()
                .uri("?q=isbn:" + isbn + "&key=" + apiKey)
                .retrieve()
                .toEntity(BookResponseElement.class)
                        .block(), "ResponseEntity is null");

        List<Book> books = Optional.ofNullable(bookResponse)
                .map(HttpEntity::getBody)
                .filter(body -> body.totalItems() >= 1)
                .map(BookResponseElement::bookItems)
                .stream()
                .flatMap(Collection::stream)
                .filter(item ->item.volumeInfo()
                        .industryIdentifiers()
                        .stream()
                        .anyMatch(isbn1 -> isbn.equals(isbn1.identifier())))
                .toList();
        return new BookResponseElement(bookResponse.getBody().totalItems(), books);

/*        List<Book> books = Optional.ofNullable(bookResponse)
                .map(HttpEntity::getBody)
                .filter(body -> body.totalItems() >= 1)
                .map(body -> {
                    if(body.totalItems() == 1) {
                        return body.bookItems();
                    } else {
                        throw new IsbnException("More then one book with the same isbn found");
                    }
                })
                .stream()
                .flatMap(bookItems -> bookItems.stream())
                .filter(item ->item.volumeInfo()
                        .industryIdentifiers()
                        .stream()
                        .anyMatch(isbn1 -> isbn.equals(isbn1.identifier())))
                .toList();*/

    }



    public BookResponseElement getAllApiBooks(String searchText) {
        ResponseEntity<BookResponseElement> bookResponse = requireNonNull(webClient
                .get()
                .uri("?q=" + searchText + "&key=" + apiKey + maxResults)
                .retrieve()
                .toEntity(BookResponseElement.class)
                .block(), "ResponseEntity is null");

        System.out.println("BOOK Response" + bookResponse);
        System.out.println("BODY Response" + bookResponse.getBody());


        List<Book> books = Optional.ofNullable(bookResponse)
                .map(HttpEntity::getBody)
                .filter(body -> body.totalItems() >= 1)
                .map(BookResponseElement::bookItems)
                .stream()
                .flatMap(Collection::stream).toList();
        return new BookResponseElement(bookResponse.getBody().totalItems(), books);



/*        return Optional.ofNullable(bookResponse)
                .map(HttpEntity::getBody)
                .map(BookResponseElement::bookItems)
                .map(bookList -> bookList.stream()
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new NoBookFoundException("No books could be found"));*/
    }


/*        BookResponseElement responseBody;
        System.out.println("BOOK Response" + bookResponse);

        if (bookResponse != null) {
            responseBody = bookResponse.getBody();
            System.out.println(" Response BODY" + responseBody);
        } else {
            throw new BookResponseException("Book Response is null");
        }
        if (responseBody != null) {
            return responseBody.bookItems();
        } else throw new BookResponseException("Response body is null");
    }*/


    public Book saveBookInDB(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

}
