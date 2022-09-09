package com.optimizely.library.csv.mappers;

import com.optimizely.library.model.Magazine;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public class MagazineMapper implements Function<Map<String, String>, Magazine> {
    @Override
    public Magazine apply(Map<String, String> map) {
        return new Magazine(
                map.get("Titel"),
                map.get("ISBN-Nummer"),
                Arrays.asList(map.get("Autor").split(",")),
                map.get("Erscheinungsdatum"));
    }
}