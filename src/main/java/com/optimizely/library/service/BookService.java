package com.optimizely.library.service;

import com.optimizely.library.model.Book;
import com.optimizely.library.model.Order;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book createBook(Book book);
    List<Book> findAllBooks();

    Optional<Book> findBookByIsbn(String isbn);

    List<Book> findBooksByAuthor(String author);

    List<Book> findBooksOrderedByTitle(Order order);
}
