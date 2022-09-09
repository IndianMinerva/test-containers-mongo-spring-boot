package com.optimizely.library.repository;

import com.optimizely.library.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
    Optional<Book> findOneByIsbn(String isbn);

    List<Book> findByAuthorsContaining(String author);
}
