package com.optimizely.library.service;

import com.optimizely.library.model.Book;
import com.optimizely.library.model.Order;
import com.optimizely.library.repository.BookRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookServiceTest {

    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

    {
        mongoDBContainer.start();
    }

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        System.out.println(mongoDBContainer.getReplicaSetUrl());
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    public void beforeEachTest() {
        bookRepository.deleteAll();
        List<Book> books = List.of(
                new Book("a title", "111-234-340", List.of("author1@library.com", "autho2@libraray.com"), "a good book"),
                new Book("z title", "112-234-341", List.of("author3@library.com", "autho2@libraray.com"), "a good book"),
                new Book("o title", "113-234-342", List.of("author2@library.com", "autho5@libraray.com"), "a good book"),
                new Book("c title", "114-234-343", List.of("author4@library.com", "autho6@libraray.com"), "a good book"),
                new Book("x title", "116-234-344", List.of("author7@library.com", "autho8@libraray.com"), "a good book")
        );
        bookRepository.insert(books);
    }


    @Test
    public void shouldInsertBook() {
        bookRepository.deleteAll();
       Book book = bookService.createBook(new Book("Title", "ISBN", List.of("author@gmail.com"), "A very good book"));
        assertEquals(bookRepository.findAll().get(0), book);
    }

    @Test
    public void shouldReturnPrePopulatedBooks() {
        List<Book> books = bookService.findAllBooks();
        assertEquals(bookRepository.findAll(), books);
    }

    @Test
    public void given_existingIsbn_shouldReturnBookByIsbn() {
        String isbn = "113-234-342";
        Optional<Book> mayBeBook = bookService.findBookByIsbn(isbn);
        mayBeBook.map(book -> {
            Assertions.assertEquals(isbn, book.getIsbn());
            return book;
        }).orElseGet(Assertions::fail);
    }

    @Test
    public void given_nonExistingIsbn_shouldReturnEmpty() {
        String isbn = "non_existant_isbn";
        Optional<Book> mayBeBook = bookService.findBookByIsbn(isbn);
        Assertions.assertTrue(mayBeBook.isEmpty());
    }

    @Test
    public void given_author_shouldReturnBooksWrittenByAuthor() {
        //given
        String author = "author2@library.com";

        //when
        List<Book> booksByAuthor = bookService.findBooksByAuthor(author);

        //then
        List<Book> expectedBooks = bookRepository.findAll()
                .stream()
                .filter(book -> book.getAuthors().contains(author))
                .toList();
        Assertions.assertEquals(expectedBooks, booksByAuthor);

    }

    @Test
    public void given_descendingOrder_shouldReturnBooksSortedByTitleByDescendingOrder() {
        //given
        Order order = Order.DESC;

        //when
        List<Book> booksSortedByTitleDesc = bookService.findBooksOrderedByTitle(order);

        //then
        List<Book> expectedBooks = bookRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(Book::getTitle)
                        .reversed())
                .toList();

        Assertions.assertEquals(expectedBooks, booksSortedByTitleDesc);

    }

    @Test
    public void given_ascendingOrder_shouldReturnBooksSortedByTitleByAscendingOrder() {
        //given
        Order order = Order.ASC;

        //when
        List<Book> booksSortedByTitleAsc = bookService.findBooksOrderedByTitle(order);

        //then
        List<Book> expectedBooks = bookRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .toList();

        Assertions.assertEquals(expectedBooks, booksSortedByTitleAsc);

    }
}
