package com.optimizely.library.controller;

import com.optimizely.library.model.Book;
import com.optimizely.library.model.Order;
import com.optimizely.library.service.BookService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksController {

    @Autowired
    private final BookService bookService;


    @PostMapping
    ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.createBook(book));
    }

    @GetMapping
    @ApiOperation("Get all the books")
    ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAllBooks());
    }

    @GetMapping("/isbn/{isbn}")
    ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        return bookService.findBookByIsbn(isbn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound()
                        .build());
    }

    @GetMapping("/author/{author}")
    ResponseEntity<List<Book>> getBookByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(bookService.findBooksByAuthor(author));
    }

    @GetMapping("/sort-by-title")
    ResponseEntity<List<Book>> getBooksByTitle(@RequestParam Order order) {
        return ResponseEntity.ok(bookService.findBooksOrderedByTitle(order));
    }
}
