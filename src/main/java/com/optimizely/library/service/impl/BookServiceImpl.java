package com.optimizely.library.service.impl;

import com.optimizely.library.model.Book;
import com.optimizely.library.model.Order;
import com.optimizely.library.repository.BookRepository;
import com.optimizely.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public Book createBook(Book book) {
        return bookRepository.insert(book);
    }

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> findBookByIsbn(String isbn) {
        return bookRepository.findOneByIsbn(isbn);
    }

    @Override
    public List<Book> findBooksByAuthor(String author) {
        return bookRepository.findByAuthorsContaining(author);
    }

    @Override
    public List<Book> findBooksOrderedByTitle(Order order) {
        Sort sort = Sort.by("title");
        Sort sortWithDirection = order.equals(Order.ASC) ? sort.ascending() : sort.descending();
        return bookRepository.findAll(sortWithDirection);
    }
}
