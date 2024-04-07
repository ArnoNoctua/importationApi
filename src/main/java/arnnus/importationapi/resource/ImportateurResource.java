package arnnus.importationapi.resource;

import arnnus.importationapi.config.UserAuthenticationProvider;
import arnnus.importationapi.domain.Importateur;
import arnnus.importationapi.domain.VinList;
import arnnus.importationapi.repo.VinRepo;
import arnnus.importationapi.service.CsvService;
import arnnus.importationapi.service.ImportateurService;
import dtos.VinListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static arnnus.importationapi.constant.Constant.PHOTO_DIRECTORY;
import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/importateurs")
@RequiredArgsConstructor
@Slf4j
//RestAPI ressource est controlleur
public class ImportateurResource {
    private final ImportateurService importateurService;
    @Autowired
    private CsvService csvService;
    @Autowired
    private VinRepo vinListRepo;

    @PostMapping
    public ResponseEntity<Importateur> createImportateur(@RequestBody Importateur importateur){
        return ResponseEntity.created(URI.create("/importateurs/userID")).body(importateurService.createImportateur(importateur));
    }

    @GetMapping
    public ResponseEntity<Page<Importateur>> getImportateurs(@RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "10") int size){
        return ResponseEntity.ok().body(importateurService.getAllImportateurs(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Importateur> getImportateur(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(importateurService.getImportateur(id));
    }

    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(importateurService.uploadPhoto(id, file));
    }

    @GetMapping(path = "/image/{filename}", produces = { IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE })
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImportateur(@PathVariable(value = "id") String id) {
        importateurService.deleteImportateur(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/csv-upload")
    public ResponseEntity<?> uploadCSV(@RequestParam("file") MultipartFile file, @RequestParam("id") String importateurId) {
        try {
            // Call service method to parse CSV file
            List<Map<String, String>> parsedData = csvService.parseCSV(file);
            // Save parsed data to database
            csvService.saveParsedDataToDatabase(importateurId, parsedData);
            // You can return the parsed data as ResponseEntity
            return ResponseEntity.ok(parsedData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload CSV file: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/vin-list")
    public ResponseEntity<List<VinListDto>> getVinListForImportateur(@PathVariable String id) {
        List<VinListDto> vinListDto = importateurService.getVinListForImportateur(id);
        ResponseEntity<List<VinListDto>> responseEntity = ResponseEntity.ok(vinListDto);
        log.info("ResponseEntity for Importateur {}: {}", id, responseEntity);
        return responseEntity;
    }

}
