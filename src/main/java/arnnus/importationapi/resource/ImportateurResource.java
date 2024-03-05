package arnnus.importationapi.resource;

import arnnus.importationapi.domain.Importateur;
import arnnus.importationapi.service.ImportateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static arnnus.importationapi.constant.Constant.PHOTO_DIRECTORY;
import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/importateurs")
@RequiredArgsConstructor
//RestAPI ressource est controlleur
public class ImportateurResource {
    private final ImportateurService importateurService;

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

}
