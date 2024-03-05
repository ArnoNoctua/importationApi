package arnnus.importationapi.service;

import arnnus.importationapi.domain.Importateur;
import arnnus.importationapi.repo.ImportateurRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static arnnus.importationapi.constant.Constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class) //si mauvais input, rollback à avant
@RequiredArgsConstructor
public class ImportateurService {
    private final ImportateurRepo importateurRepo; //Injection de dépendances

    public Page<Importateur> getAllImportateurs(int page, int size){
        return importateurRepo.findAll(PageRequest.of(page, size, Sort.by("name")));
    }

    public Importateur getImportateur(String id) {
        return importateurRepo.findById(id).orElseThrow(() -> new RuntimeException("Importateur pas trouvé"));
    }

    public Importateur createImportateur(Importateur importateur) {
        return importateurRepo.save(importateur);
    }

    public void deleteImportateur(String id) {
        Importateur importateur = importateurRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Importateur non trouvé avec cet id: " + id));
        importateurRepo.delete(importateur);
    }

    public String uploadPhoto(String id, MultipartFile file) {
        log.info("L'image pour le user id a été sauvegardée : {}",id);
        Importateur importateur = getImportateur(id);
        String photoUrl = photoFunction.apply(id, file); //photoFunction va retourner un id et l'extension ficher en string
        importateur.setPhotoURL(photoUrl);
        importateurRepo.save(importateur);
        return photoUrl;
    }

    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.indexOf(".") + 1)).orElse(".png");

    //BiFunction prend un string et un multipartfile et va retourner un string
    private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {
        String filename = id + fileExtension.apply(image.getOriginalFilename());
        try {
            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if(!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }
            //Le but est de sauvegarder le fichier pas sur user uuid
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), REPLACE_EXISTING); //photoFunction va retourner un id et l'extension ficher en string
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/importateurs/image/" + filename).toUriString();
        } catch (Exception exception){
            throw  new RuntimeException("Impossible de sauvegarder l'image");
        }
    };
}
