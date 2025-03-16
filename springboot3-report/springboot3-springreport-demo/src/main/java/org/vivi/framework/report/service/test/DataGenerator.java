package org.vivi.framework.report.service.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.vivi.framework.report.service.web.dto.reporttpl.ResPreviewData;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class DataGenerator {

    public List<Map<String, Object>> readJsonFile(String fileName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new IOException("File not found: " + fileName);
            }
            return objectMapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>() {
            });
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResPreviewData readJsonToPojo(String fileName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new IOException("File not found: " + fileName);
            }
            return objectMapper.readValue(inputStream, ResPreviewData.class);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        DataGenerator dataGenerator = new DataGenerator();
        //List<Map<String, Object>> data = dataGenerator.readJsonFile("param/chart_preview_data.json");
        //data.forEach(System.out::println);

        //ResPreviewData resPreviewData = dataGenerator.readJsonToPojo("param/chart_preview_data.json");
        //System.out.println(resPreviewData);
    }
}
