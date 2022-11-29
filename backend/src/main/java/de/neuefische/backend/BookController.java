package de.neuefische.backend;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping("/")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    //    @GetMapping("{searchText}")
    @GetMapping("/search/{searchText}")
    public List<Book> getAllApiBooks(@PathVariable String searchText) {
        if (!searchText.isEmpty()) {
            return bookService.getAllApiBooks(searchText);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/isbn/{isbn}")
    public List<Book> getApiBookByIsbn(@PathVariable @Valid String isbn) {
        return bookService.getApiBookByIsbn(isbn);
    }


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Book saveBookInDB(@RequestBody Book book) {
        return bookService.saveBookInDB(book);
    }


    // Save book with ISBN in URL and in Body as book
/*
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
*/



/*    @PostMapping()
    public ResponseEntity<String> saveApiBooksToDb() {
        try {
            bookService.saveApiBooksToDB();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (BookResponseException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }*/

}
