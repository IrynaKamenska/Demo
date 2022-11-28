package de.neuefische.backend;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {
    private final BookService bookService;


    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{searchText}")
    public List<Book> getAllApiBooks(String searchText) {
        return bookService.getAllApiBooks(searchText);
    }

    @GetMapping("/isbn/{isbn}")
    public Book getApiBookByIsbn(@PathVariable String isbn) {
        return bookService.getApiBookByIsbn(isbn);
    }

    // Save book with ISBN in URL and in Body as book
    @PostMapping("/{isbnToFind}")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveBookWithIsbnAndBodyInDB(@PathVariable String isbnToFind, @RequestBody Book book) {
        List<Isbn> isbnList = book.volumeInfo().industryIdentifiers();
        for (Isbn isbn : isbnList) {
            if (isbn.identifier().equals(isbnToFind)) {
                try {
                    bookService.saveBookWithIsbnAndBodyInDB(isbnToFind, book);
                } catch (BookAlreadyExistException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
                }
            }
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }




    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Book saveBookWithIsbnInDB(@RequestBody Book book) {
           return bookService.saveBookWithIsbnInDB(book);
    }


    @PostMapping()
    public ResponseEntity<String> saveApiBooksToDb() {
        try {
            bookService.saveApiBooksToDB();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (BookResponseException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
