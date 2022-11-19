package de.neuefische.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookLibraryService {

    private final BookLibraryRepository bookLibraryRepository;


    public List<Book> getBooks() {
       return bookLibraryRepository.findAll();
    }

    public Book findBookByTitle(String title){

        if(title.contains(null)){
            throw new NoBookFoundException("No book with title " + title + "found" );
        }
        return bookLibraryRepository.findBookByTitle(title);
    }

    public Book addBook(Book book) {
        return bookLibraryRepository.save(book);
    }
}
