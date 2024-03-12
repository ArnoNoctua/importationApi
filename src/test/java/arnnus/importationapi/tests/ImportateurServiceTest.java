package arnnus.importationapi.tests;

import arnnus.importationapi.domain.Importateur;
import arnnus.importationapi.repo.ImportateurRepo;
import arnnus.importationapi.service.ImportateurService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImportateurServiceTest {

    @Test
    void testGetAllImportateurs() {
        ImportateurRepo importateurRepoMock = mock(ImportateurRepo.class);
        ImportateurService importateurService = new ImportateurService(importateurRepoMock);

        Page<Importateur> page = new PageImpl<>(Collections.singletonList(new Importateur()));
        when(importateurRepoMock.findAll(any(PageRequest.class))).thenReturn(page);

        assertEquals(page, importateurService.getAllImportateurs(0, 10));
    }

    @Test
    void testGetImportateur() {
        // Mocking data
        String importateurId = "1";
        Importateur sampleImportateur = new Importateur();
        sampleImportateur.setId(importateurId);
        sampleImportateur.setName("Sample Importateur");

        // Mocking behavior
        ImportateurRepo importateurRepoMock = mock(ImportateurRepo.class);
        ImportateurService importateurService = new ImportateurService(importateurRepoMock);
        when(importateurRepoMock.findById(importateurId)).thenReturn(Optional.of(sampleImportateur));

        // Invoking the method
        Importateur result = importateurService.getImportateur(importateurId);

        // Assertions
        assertEquals(sampleImportateur, result);
    }

    @Test
    void testCreateImportateur() {
        // Mocking data
        Importateur importateurToCreate = new Importateur();
        importateurToCreate.setId("1");
        importateurToCreate.setName("Sample Importateur");

        // Mocking behavior
        ImportateurRepo importateurRepoMock = mock(ImportateurRepo.class);
        ImportateurService importateurService = new ImportateurService(importateurRepoMock);
        when(importateurRepoMock.save(importateurToCreate)).thenReturn(importateurToCreate);

        // Invoking the method
        Importateur result = importateurService.createImportateur(importateurToCreate);

        // Assertions
        assertEquals(importateurToCreate, result);
    }

    @Test
    void testDeleteImportateur() {
        // Mocking data
        String importateurId = "1";

        // Mocking behavior
        ImportateurRepo importateurRepoMock = mock(ImportateurRepo.class);
        ImportateurService importateurService = new ImportateurService(importateurRepoMock);
        when(importateurRepoMock.findById(importateurId)).thenReturn(Optional.empty());

        // Invoking the method
        assertThrows(RuntimeException.class, () -> importateurService.deleteImportateur(importateurId));

        // Verifying behavior
        Mockito.verify(importateurRepoMock, Mockito.never()).deleteById(importateurId);
    }
}

