package com.optimizely.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optimizely.library.model.Book;
import com.optimizely.library.model.Order;
import com.optimizely.library.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {
    @MockBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /books Test - Success")
    public void testInsertBook() throws Exception {
        //given
        when(bookService.createBook(any())).thenReturn(getBooks().get(0));
        //when
        var resultActions = mockMvc.perform(post("/books", "id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getBooks().get(0))));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(asJsonString(getBooks().get(0))));
    }

    @Test
    @DisplayName("GET /books Test - Success")
    public void testGetAllBooks() throws Exception {
        //given
        when(bookService.findAllBooks()).thenReturn(getBooks());

        //when
        var resultActions = mockMvc.perform(get("/books", "id"));
        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(asJsonString(getBooks())));
    }

    @Test
    @DisplayName("GET /books/{isbn} - Success")
    public void testGetBooksByIsbnSuccess() throws Exception {
        //given
        String isbn = "111-234-340";
        Optional<Book> mayBeBook = getBooks().stream().filter(book -> book.getIsbn().equals(isbn)).findFirst();
        when(bookService.findBookByIsbn(isbn))
                .thenReturn(mayBeBook);

        //given
        var resultActions = mockMvc.perform(get("/books/isbn/" + isbn));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(asJsonString(mayBeBook.get())));
    }

    @Test
    @DisplayName("GET /books/{isbn} - Success")
    public void testGetBooksByIsbnFailure() throws Exception {
        //given
        String isbn = "111-234-340";
        when(bookService.findBookByIsbn(isbn))
                .thenReturn(Optional.empty());

        //given
        var resultActions = mockMvc.perform(get("/books/isbn/" + isbn));

        //then
        resultActions
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /books/{author} - Success")
    public void testGetBooksByAuthorSuccess() throws Exception {
        //given
        String author = "author2@library.com";
        List<Book> booksByAuthor = bookService.findBooksByAuthor(author).stream().filter(book -> book.getAuthors().contains(author)).toList();
        when(bookService.findBooksByAuthor(author))
                .thenReturn(booksByAuthor);

        //given
        var resultActions = mockMvc.perform(get("/books/author/" + author));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(asJsonString(booksByAuthor)));
    }

    @Test
    @DisplayName("GET /books/sort-by-title?order=ASC - Success")
    public void testSortByTitleAscSuccess() throws Exception {
        //given
        Order order = Order.ASC;
        List<Book> sortedByTitleAsc = bookService.findBooksOrderedByTitle(order)
                .stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .toList();
        when(bookService.findBooksOrderedByTitle(order))
                .thenReturn(sortedByTitleAsc);

        //given
        var resultActions = mockMvc.perform(get("/books/sort-by-title?order=" + order));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(asJsonString(sortedByTitleAsc)));
    }


    @Test
    @DisplayName("GET /books/sort-by-title?order=DESC - Success")
    public void testSortByTitleDescSuccess() throws Exception {
        //given
        Order order = Order.DESC;
        List<Book> sortedByTitleDesc = bookService.findBooksOrderedByTitle(order)
                .stream()
                .sorted(Comparator.comparing(Book::getTitle).reversed())
                .toList();
        when(bookService.findBooksOrderedByTitle(order))
                .thenReturn(sortedByTitleDesc);

        //given
        var resultActions = mockMvc.perform(get("/books/sort-by-title?order=" + order));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(asJsonString(sortedByTitleDesc)));
    }


    @Test
    @DisplayName("GET /books/sort-by-title?order=XYZ - Failure")
    public void testSortByTitleFailure() throws Exception {
        //given
        //when
        var resultActions = mockMvc.perform(get("/books/sort-by-title?order=XYZ"));

        //then
        resultActions
                .andExpect(status().isBadRequest());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Book> getBooks() {
        return List.of(
                new Book("a title", "111-234-340", List.of("author1@library.com", "autho2@libraray.com"), "a good book"),
                new Book("z title", "112-234-341", List.of("author3@library.com", "autho2@libraray.com"), "a good book"),
                new Book("o title", "113-234-342", List.of("author2@library.com", "autho5@libraray.com"), "a good book"),
                new Book("c title", "114-234-343", List.of("author4@library.com", "autho6@libraray.com"), "a good book"),
                new Book("x title", "116-234-344", List.of("author7@library.com", "autho8@libraray.com"), "a good book")
        );
    }
}