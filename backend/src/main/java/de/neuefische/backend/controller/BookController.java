package de.neuefische.backend.controller;

import de.neuefische.backend.model.BookResponseElement;
import de.neuefische.backend.model.Book;
import de.neuefische.backend.service.BookService;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;


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


    @GetMapping("/search/{searchText}")
    public BookResponseElement getAllApiBooks(@PathVariable String searchText) {
        return bookService.getAllApiBooks(searchText);
    }

    @GetMapping("/isbn/{isbn}")
    public BookResponseElement getApiBookByIsbn(@PathVariable @Valid String isbn) {
        return bookService.getApiBookByIsbn(isbn);
    }


    @PostMapping("/")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Book saveBookInDB(@RequestBody Book book) {
        return bookService.saveBookInDB(book);
    }



}
