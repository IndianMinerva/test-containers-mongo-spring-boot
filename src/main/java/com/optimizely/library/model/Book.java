package com.optimizely.library.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class Book {
    private final String title;
    @Indexed(unique = true)
    private final String isbn;
    private final List<String> authors;
    private final String description;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof Book book) {
            return book.getIsbn().equals(this.getIsbn());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Book {" +
                "title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", authors=" + authors +
                ", description='" + description + '\'' +
                '}';
    }
}
