package de.neuefische.backend;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface BookRepository extends MongoRepository<Book, String> {

//    public Book findBookByTitle(String title);
}
