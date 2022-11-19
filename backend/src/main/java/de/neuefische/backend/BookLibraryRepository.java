package de.neuefische.backend;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface BookLibraryRepository extends MongoRepository<Book, String> {

    public Book findBookByTitle(String title);
}
