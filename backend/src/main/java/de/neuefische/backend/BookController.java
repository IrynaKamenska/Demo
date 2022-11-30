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
    public BookResponseElement getAllApiBooks(@PathVariable String searchText) {
        if (!searchText.isEmpty()) {
            return bookService.getAllApiBooks(searchText);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/isbn/{isbn}")
    public BookResponseElement getApiBookByIsbn(@PathVariable @Valid String isbn) {
        return bookService.getApiBookByIsbn(isbn);
    }


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Book saveBookInDB(@RequestBody Book book) {
        return bookService.saveBookInDB(book);
    }



}
