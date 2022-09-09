package com.optimizely.library.controller;

import com.optimizely.library.model.Magazine;
import com.optimizely.library.model.Order;
import com.optimizely.library.service.MagazineService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/magazines")
@RequiredArgsConstructor
public class MagazinesController {

    private final MagazineService magazineService;

    @PostMapping
    ResponseEntity<Magazine> crateMagazine(@RequestBody Magazine magazine) {
        return ResponseEntity.ok(magazineService.createMagazine(magazine));
    }

    @GetMapping
    @ApiOperation("Get all the magazines")
    public ResponseEntity<List<Magazine>> getAllMagazines() {
        return ResponseEntity.ok(magazineService.findAllMagazines());
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Magazine> getMagazineByIsbn(@PathVariable String isbn) {
        return magazineService.findMagazineByIsbn(isbn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound()
                        .build());
    }

    @GetMapping("/author/{author}")
    ResponseEntity<List<Magazine>> getBookByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(magazineService.findMagazinesByAuthor(author));
    }

    @GetMapping("/sort-by-title")
    ResponseEntity<List<Magazine>> getBooksByTitle(@RequestParam Order order) {
        return ResponseEntity.ok(magazineService.findMagazinesOrderedByTitle(order));
    }
}
