package com.optimizely.library.csv.mappers;

import com.optimizely.library.model.Author;

import java.util.Map;
import java.util.function.Function;

public class AuthorMapper implements Function<Map<String, String>, Author> {
    @Override
    public Author apply(Map<String, String> map) {
        return new Author(
                map.get("Emailadresse"),
                map.get("Vorname"),
                map.get("Nachname"));
    }
}