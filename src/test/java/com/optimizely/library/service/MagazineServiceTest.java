package com.optimizely.library.service;

import com.optimizely.library.model.Book;
import com.optimizely.library.model.Magazine;
import com.optimizely.library.model.Order;
import com.optimizely.library.repository.MagazineRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MagazineServiceTest {

    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

    {
        mongoDBContainer.start();
    }

    @Autowired
    private MagazineRepository magazineRepository;

    @Autowired
    private MagazineService magazineService;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }


    @BeforeEach
    public void beforeEachTest() {
        magazineRepository.deleteAll();
        List<Magazine> magazines = List.of(
                new Magazine("a title", "111-234-340", List.of("author1@library.com", "autho2@libraray.com"), "01-01-2022"),
                new Magazine("z title", "112-234-341", List.of("author3@library.com", "autho2@libraray.com"), "01-01-2022"),
                new Magazine("o title", "113-234-342", List.of("author2@library.com", "autho5@libraray.com"), "01-01-2022"),
                new Magazine("c title", "114-234-343", List.of("author4@library.com", "autho6@libraray.com"), "01-01-2022"),
                new Magazine("x title", "116-234-344", List.of("author7@library.com", "autho8@libraray.com"), "01-01-2022")
        );
        magazineRepository.insert(magazines);
    }

    @Test
    public void shouldInsertMagazine() {
        magazineRepository.deleteAll();
        Magazine magazine = magazineService.createMagazine(new Magazine("Title", "ISBN", List.of("author@gmail.com"), "01.01.1104"));
        assertEquals(magazineRepository.findAll().get(0), magazine);
    }

    @Test
    public void shouldReturnPrePopulatedMagazines() {
        List<Magazine> magazines = magazineRepository.findAll();
        assertEquals(5, magazines.size());
    }

    @Test
    public void given_existingIsbn_shouldReturnMagazineByIsbn() {
        String isbn = "113-234-342";
        Optional<Magazine> mayBeMagazine = magazineService.findMagazineByIsbn(isbn);
        mayBeMagazine.map(magazine -> {
            Assertions.assertEquals(isbn, magazine.getIsbn());
            return magazine;
        }).orElseGet(Assertions::fail);
    }

    @Test
    public void given_nonExistingIsbn_shouldReturnEmpty() {
        String isbn = "non_existant_isbn";
        Optional<Magazine> mayBeMagazine = magazineService.findMagazineByIsbn(isbn);
        Assertions.assertTrue(mayBeMagazine.isEmpty());
    }

    @Test
    public void given_author_shouldReturnMagazineWrittenByAuthor() {
        //given
        String author = "author2@library.com";

        //when
        List<Magazine> magazinesByAuthor = magazineService.findMagazinesByAuthor(author);

        //then
        List<Magazine> expectedMagazines = magazineRepository.findAll()
                .stream()
                .filter(magazine -> magazine.getAuthors().contains(author))
                .toList();
        Assertions.assertEquals(expectedMagazines, magazinesByAuthor);

    }

    @Test
    public void given_descendingOrder_shouldReturnMagazinesSortedByTitleByDescendingOrder() {
        //given
        Order order = Order.DESC;

        //when
        List<Magazine> magazinesSortedByTitleDesc = magazineService.findMagazinesOrderedByTitle(order);

        //then
        List<Magazine> expectedMagazines = magazineRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(Magazine::getTitle)
                        .reversed())
                .toList();

        Assertions.assertEquals(expectedMagazines, magazinesSortedByTitleDesc);

    }

    @Test
    public void given_ascendingOrder_shouldReturnMagazinesSortedByTitleByAscendingOrder() {
        //given
        Order order = Order.ASC;

        //when
        List<Magazine> magazinesSortedByTitleAsc = magazineService.findMagazinesOrderedByTitle(order);

        //then
        List<Magazine> expectedMagazines = magazineRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(Magazine::getTitle))
                .toList();

        Assertions.assertEquals(expectedMagazines, magazinesSortedByTitleAsc);

    }
}
