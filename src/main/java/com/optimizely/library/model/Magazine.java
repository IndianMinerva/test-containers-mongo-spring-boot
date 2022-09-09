package com.optimizely.library.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class Magazine {
    private final String title;
    private final String isbn;
    private final List<String> authors;
    private final String publicationDate;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof Magazine magazine) {
            return this.getIsbn().equals(magazine.getIsbn());
        }
        return false;
    }
}
