package com.optimizely.library.csv.mappers;

import com.optimizely.library.model.Book;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public class BookMapper implements Function<Map<String, String>, Book> {
    @Override
    public Book apply(Map<String, String> map) {
        return new Book(
                map.get("Titel"),
                map.get("ISBN-Nummer"),
                Arrays.asList(map.get("Autoren").split(",")),
                map.get("Kurzbeschreibung"));
    }
}