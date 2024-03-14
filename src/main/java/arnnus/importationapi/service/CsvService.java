package arnnus.importationapi.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CsvService {

    private static final Logger logger = LoggerFactory.getLogger(CsvService.class);

    public List<Map<String, String>> parseCSV(MultipartFile file) throws IOException, CsvValidationException {
        List<Map<String, String>> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] headers = reader.readNext(); // Assuming first row contains headers
            String[] line;
            while ((line = reader.readNext()) != null) {
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < headers.length && i < line.length; i++) {
                    row.put(headers[i], line[i]);
                }
                data.add(row);
            }
        }

        // Log the parsed data
        logger.info("Parsed CSV data: {}", data);

        return data;
    }
}




