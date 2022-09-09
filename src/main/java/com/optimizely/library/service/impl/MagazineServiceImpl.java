package com.optimizely.library.service.impl;

import com.optimizely.library.model.Magazine;
import com.optimizely.library.model.Order;
import com.optimizely.library.repository.MagazineRepository;
import com.optimizely.library.service.MagazineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MagazineServiceImpl implements MagazineService {
    private final MagazineRepository magazineRepository;

    @Override
    public Magazine createMagazine(Magazine magazine) {
        return magazineRepository.insert(magazine);
    }

    @Override
    public List<Magazine> findAllMagazines() {
        return magazineRepository.findAll();
    }

    @Override
    public Optional<Magazine> findMagazineByIsbn(String isbn) {
        return magazineRepository.findOneByIsbn(isbn);
    }

    @Override
    public List<Magazine> findMagazinesByAuthor(String author) {
        return magazineRepository.findByAuthorsContaining(author);
    }

    @Override
    public List<Magazine> findMagazinesOrderedByTitle(Order order) {
        Sort sort = Sort.by("title");
        Sort sortWithDirection = order.equals(Order.ASC) ? sort.ascending() : sort.descending();
        return magazineRepository.findAll(sortWithDirection);
    }
}
