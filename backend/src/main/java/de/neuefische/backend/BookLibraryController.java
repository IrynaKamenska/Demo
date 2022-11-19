package de.neuefische.backend;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookLibraryController {
    private final BookLibraryService bookLibraryService;


    @GetMapping
    public List<Book> getBooks() {
        return bookLibraryService.getBooks();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Book addBook(@RequestBody Book book){
       return bookLibraryService.addBook(book);
    }
}
