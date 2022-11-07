package com.optimizely.library.csv;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;


@Slf4j
@Component
public class CSVDataLoader {
    public <T> Optional<List<T>> csvToObjects(String fileName, Function<Map<String, String>, T> mapper) {

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(String.valueOf(getClass().getClassLoader().getResource("data/" + fileName)))));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.newFormat(';').withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            var csvRecords = csvParser.getRecords()
                    .stream().map(CSVRecord::toMap).toList();
            List<T> entities = csvRecords.stream()
                    .map(mapper).collect(toList());

            return Optional.of(entities);
        } catch (IOException e) {
            log.error("Error occurred parsing CSV {} {}", fileName, e);
            return Optional.empty();
        }
    }
}