package com.optimizely.library.service;

import com.optimizely.library.model.Magazine;
import com.optimizely.library.model.Order;

import java.util.List;
import java.util.Optional;

public interface MagazineService {

    Magazine createMagazine(Magazine magazine);

    List<Magazine> findAllMagazines();

    Optional<Magazine> findMagazineByIsbn(String isbn);

    List<Magazine> findMagazinesByAuthor(String author);

    List<Magazine> findMagazinesOrderedByTitle(Order order);
}
