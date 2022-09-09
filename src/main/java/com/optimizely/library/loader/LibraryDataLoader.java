package com.optimizely.library.loader;

import com.optimizely.library.csv.CSVDataLoader;
import com.optimizely.library.csv.mappers.AuthorMapper;
import com.optimizely.library.csv.mappers.BookMapper;
import com.optimizely.library.csv.mappers.MagazineMapper;
import com.optimizely.library.repository.AuthorRepository;
import com.optimizely.library.repository.BookRepository;
import com.optimizely.library.repository.MagazineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

@Slf4j
public class LibraryDataLoader implements ApplicationRunner {
    @Autowired
    CSVDataLoader csvDataLoader;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    MagazineRepository magazineRepository;


    @Autowired
    BookRepository bookRepository;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Loading the data from CSV files");
        loadAuthors();
        loadAuthors();
        loadBooks();
        loadMagazines();
        log.info("Loading the data from CSV files completed");
    }

    private void loadAuthors() {
        authorRepository.deleteAll();
        csvDataLoader
                .csvToObjects("autoren.csv", new AuthorMapper())
                .ifPresent(authors -> authors.forEach(author -> authorRepository.save(author)));
    }

    public void loadBooks() {
        bookRepository.deleteAll();
        csvDataLoader
                .csvToObjects("buecher.csv", new BookMapper())
                .ifPresent(books -> books.forEach(book -> bookRepository.save(book)));
    }

    private void loadMagazines() {
        magazineRepository.deleteAll();
        csvDataLoader
                .csvToObjects("zeitschriften.csv", new MagazineMapper())
                .ifPresent(magazines -> magazines.forEach(book -> magazineRepository.save(book)));
    }
}
