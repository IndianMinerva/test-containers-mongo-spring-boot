package com.optimizely.library.repository;

import com.optimizely.library.model.Magazine;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MagazineRepository extends MongoRepository<Magazine, String> {
    Optional<Magazine> findOneByIsbn(String isbn);

    List<Magazine> findByAuthorsContaining(String author);
}
