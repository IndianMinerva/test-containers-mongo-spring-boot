package com.optimizely.library.config;

import com.optimizely.library.csv.CSVDataLoader;
import com.optimizely.library.csv.mappers.AuthorMapper;
import com.optimizely.library.csv.mappers.BookMapper;
import com.optimizely.library.csv.mappers.MagazineMapper;
import com.optimizely.library.repository.AuthorRepository;
import com.optimizely.library.repository.BookRepository;
import com.optimizely.library.repository.MagazineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class LibraryDataLoader {
    @Autowired
    CSVDataLoader csvDataLoader;

    @Value("classpath:data/buecher.csv")
    private Resource booksData;

    @Value("classpath:data/autoren.csv")
    private Resource authorsData;

    @Value("classpath:data/zeitschriften.csv")
    private Resource magazinesData;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    MagazineRepository magazineRepository;


    @Autowired
    BookRepository bookRepository;

    @Bean
    @Autowired
    public void run() {
        log.info("Loading the data from CSV files");
        loadAuthors();
        loadBooks();
        loadMagazines();
        log.info("Loading the data from CSV files completed");
    }

    private void loadAuthors() {
        authorRepository.deleteAll();
        try {
            csvDataLoader
                    .csvToObjects(authorsData.getFile().getAbsolutePath(), new AuthorMapper())
                    .ifPresent(authors -> authors.forEach(author -> authorRepository.save(author)));
        } catch (IOException e) {
            log.error("Exception occurred processing the authors");
        }
    }

    public void loadBooks() {
        bookRepository.deleteAll();
        try {
            csvDataLoader
                    .csvToObjects(booksData.getFile().getAbsolutePath(), new BookMapper())
                    .ifPresent(books -> books.forEach(book -> bookRepository.save(book)));
        } catch (IOException e) {
            log.error("Exception occurred processing the books");
        }
    }

    private void loadMagazines() {
        magazineRepository.deleteAll();
        try {
        csvDataLoader
                .csvToObjects(magazinesData.getFile().getAbsolutePath(), new MagazineMapper())
                .ifPresent(magazines -> magazines.forEach(magazine -> magazineRepository.save(magazine)));
        } catch (IOException e) {
            log.error("Exception occurred processing the authors");
        }
    }
}
