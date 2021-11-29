package com.example.seek.utility;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileParserTest {

    @SneakyThrows
    @Test
    void parseTrafficCounterFile() {
        FileParser fileParser = new FileParser();
        String path = "src/test/resources/input.txt";

        File file = new File(path);
        String absolutePath = file.getAbsolutePath();

        List<String> result = fileParser.parseTrafficCounterFile(absolutePath);
        assertEquals(24, result.size());
    }

    @SneakyThrows
    @Test
    void parseEmptyFile() {
        FileParser fileParser = new FileParser();
        String path = "src/test/resources/empty.txt";

        File file = new File(path);
        String absolutePath = file.getAbsolutePath();

        List<String> result = fileParser.parseTrafficCounterFile(absolutePath);
        assertEquals(0, result.size());
    }
}