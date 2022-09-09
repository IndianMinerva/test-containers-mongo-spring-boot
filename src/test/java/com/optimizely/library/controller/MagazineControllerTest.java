package com.optimizely.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optimizely.library.model.Magazine;
import com.optimizely.library.model.Order;
import com.optimizely.library.service.MagazineService;
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
public class MagazineControllerTest {
    @MockBean
    private MagazineService magazineService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /magazines Test - Success")
    public void testInsertMagazine() throws Exception {
        //given
        when(magazineService.createMagazine(any())).thenReturn(getMagazines().get(0));
        //when
        var resultActions = mockMvc.perform(post("/magazines", "id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getMagazines().get(0))));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(asJsonString(getMagazines().get(0))));
    }

    @Test
    @DisplayName("GET /magazines Test - Success")
    public void testGetAllMagazines() throws Exception {
        //given
        when(magazineService.findAllMagazines()).thenReturn(getMagazines());

        //when
        var resultActions = mockMvc.perform(get("/magazines", "id"));
        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(asJsonString(getMagazines())));
    }

    @Test
    @DisplayName("GET /magazines/isbn/{isbn} - Success")
    public void testGetMagazinesByIsbnSuccess() throws Exception {
        //given
        String isbn = "111-234-340";
        Optional<Magazine> mayBeMagazine = getMagazines().stream().filter(magazine -> magazine.getIsbn().equals(isbn)).findFirst();
        when(magazineService.findMagazineByIsbn(isbn))
                .thenReturn(mayBeMagazine);

        //given
        var resultActions = mockMvc.perform(get("/magazines/isbn/" + isbn));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(asJsonString(mayBeMagazine.get())));
    }

    @Test
    @DisplayName("GET /magazines/isbn/{isbn} - Failure")
    public void testGetMagazinesByIsbnFailure() throws Exception {
        //given
        String isbn = "111-234-340";
        when(magazineService.findMagazineByIsbn(isbn))
                .thenReturn(Optional.empty());

        //given
        var resultActions = mockMvc.perform(get("/magazines/isbn/" + isbn));

        //then
        resultActions
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /magazines/author/{author} - Success")
    public void testGetMagazinesByAuthorSuccess() throws Exception {
        //given
        String author = "author2@library.com";
        List<Magazine> magazinesByAuthor = magazineService
                .findMagazinesByAuthor(author)
                .stream()
                .filter(magazine -> magazine.getAuthors().contains(author))
                .toList();
        when(magazineService.findMagazinesByAuthor(author))
                .thenReturn(magazinesByAuthor);

        //given
        var resultActions = mockMvc.perform(get("/magazines/author/" + author));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(asJsonString(magazinesByAuthor)));
    }

    @Test
    @DisplayName("GET /magazines/sort-by-title?order=ASC - Success")
    public void testSortByTitleAscSuccess() throws Exception {
        //given
        Order order = Order.ASC;
        List<Magazine> sortedByTitleAsc = magazineService.findMagazinesOrderedByTitle(order)
                .stream()
                .sorted(Comparator.comparing(Magazine::getTitle))
                .toList();
        when(magazineService.findMagazinesOrderedByTitle(order))
                .thenReturn(sortedByTitleAsc);

        //given
        var resultActions = mockMvc.perform(get("/magazines/sort-by-title?order=" + order));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(asJsonString(sortedByTitleAsc)));
    }


    @Test
    @DisplayName("GET /magazines/sort-by-title?order=DESC - Success")
    public void testSortByTitleDescSuccess() throws Exception {
        //given
        Order order = Order.DESC;
        List<Magazine> sortedByTitleDesc = magazineService.findMagazinesOrderedByTitle(order)
                .stream()
                .sorted(Comparator.comparing(Magazine::getTitle).reversed())
                .toList();
        when(magazineService.findMagazinesOrderedByTitle(order))
                .thenReturn(sortedByTitleDesc);

        //given
        var resultActions = mockMvc.perform(get("/magazines/sort-by-title?order=" + order));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(asJsonString(sortedByTitleDesc)));
    }


    @Test
    @DisplayName("GET /magazines/sort-by-title?order=XYZ - Failure")
    public void testSortByTitleFailure() throws Exception {
        //given
        //when
        var resultActions = mockMvc.perform(get("/magazines/sort-by-title?order=XYZ"));

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

    private List<Magazine> getMagazines() {
        return List.of(
                new Magazine("a title", "111-234-340", List.of("author1@library.com", "autho2@libraray.com"), "01-01-2020"),
                new Magazine("z title", "112-234-341", List.of("author3@library.com", "autho2@libraray.com"), "01-01-2020"),
                new Magazine("o title", "113-234-342", List.of("author2@library.com", "autho5@libraray.com"), "01-01-2020"),
                new Magazine("c title", "114-234-343", List.of("author4@library.com", "autho6@libraray.com"), "01-01-2020"),
                new Magazine("x title", "116-234-344", List.of("author7@library.com", "autho8@libraray.com"), "01-01-2020")
        );
    }
}