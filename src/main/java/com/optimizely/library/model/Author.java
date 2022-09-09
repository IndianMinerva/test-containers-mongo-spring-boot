package com.optimizely.library.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Author {
    private final String email;
    private final String firstName;
    private final String lastName;
}
