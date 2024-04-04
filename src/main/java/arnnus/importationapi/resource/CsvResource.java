package arnnus.importationapi.resource;

import arnnus.importationapi.service.CsvService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
public class CsvResource {
    private final CsvService csvService;

    public CsvResource(CsvService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadCSV(@RequestParam("file") MultipartFile file, @RequestParam("id") String importateurId) {
        try {
            // Call service method to parse CSV file
            List<Map<String, String>> parsedData = csvService.parseCSV(file);

            // Save the parsed data to the database
            csvService.saveParsedDataToDatabase(importateurId, parsedData);

            // You can return the parsed data as ResponseEntity
            return ResponseEntity.ok(parsedData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload CSV file: " + e.getMessage());
        }
    }
}

