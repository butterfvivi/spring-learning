package org.vivi.framework.iexceltoolkit.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class DataGenerator {

    public List<Map<String, Object>> readJsonFile(String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IOException("File not found: " + fileName);
        }
        return objectMapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>() {});
    }

    public static void main(String[] args) {
        DataGenerator dataGenerator = new DataGenerator();
        try {
            List<Map<String, Object>> data = dataGenerator.readJsonFile("json/genProducts.json");
            data.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
