package de.neuefische.backend.repository;

import de.neuefische.backend.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface BookRepository extends MongoRepository<Book, String> {

//    public Book findBookByTitle(String title);
}
