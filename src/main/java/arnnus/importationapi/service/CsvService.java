package arnnus.importationapi.service;

import arnnus.importationapi.domain.Importateur;
import arnnus.importationapi.domain.VinList;
import arnnus.importationapi.repo.VinRepo;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
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
    private final VinRepo vinRepo;
    private final ImportateurService importateurService;

    public CsvService(VinRepo vinRepo, ImportateurService importateurService) {
        this.vinRepo = vinRepo;
        this.importateurService = importateurService;
    }

    public List<Map<String, String>> parseCSV(MultipartFile file) throws IOException, CsvValidationException {
        List<Map<String, String>> data = new ArrayList<>();
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream())).withCSVParser(parser).build()) {
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

        // Check if the data is correctly parsed
        for (Map<String, String> row : data) {
            if (!(row.containsKey("Nom") && row.containsKey("Millesime") && row.containsKey("Pays") && row.containsKey("Region") && row.containsKey("Prix") && row.containsKey("Quantite"))) {
                logger.error("CSV file not correctly parsed: {}", row);
            }
        }

        return data;
    }

    public void saveParsedDataToDatabase(String importateurId, List<Map<String, String>> parsedData) {
        Importateur importateur = importateurService.getImportateur(importateurId);
        List<VinList> vinList = new ArrayList<>();

        for (Map<String, String> row : parsedData) {
            logger.info("Row data: {}", row); // Log the row data

            VinList vin = new VinList();
            vin.setImportateurId(importateurId);
            vin.setImportateur(importateur);

            String nom = row.get("Nom");
            if (nom != null) {
                vin.setNom(nom.trim());
                logger.info("Nom: {}", nom);
            }

            String millesime = row.get("Millesime");
            if (millesime != null) {
                vin.setMillesime(millesime.trim());
            }

            String pays = row.get("Pays");
            if (pays != null) {
                vin.setPays(pays.trim());
                logger.info("Pays: {}", pays);
            }

            String region = row.get("Region");
            if (region != null) {
                vin.setRegion(region.trim());
            }

            String prix = row.get("Prix");
            if (prix != null) {
                vin.setPrix(Double.valueOf(prix.trim()));
            }

            String quantite = row.get("Quantite");
            if (quantite != null) {
                vin.setQuantite(Integer.valueOf(quantite.trim()));
            }

            vinList.add(vin);
        }

        logger.info("VinList before save: {}", vinList); // Log the VinList before the save operation

        try {
            vinRepo.saveAll(vinList);
        } catch (Exception e) {
            logger.error("Error occurred during save operation", e); // Log any exceptions that occur during the save operation
        }

        logger.info("VinList after save: {}", vinList); // Log the VinList after the save operation
    }
}




