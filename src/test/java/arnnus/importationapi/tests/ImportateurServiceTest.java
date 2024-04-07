package arnnus.importationapi.tests;

import arnnus.importationapi.domain.Importateur;
import arnnus.importationapi.domain.VinList;
import arnnus.importationapi.repo.ImportateurRepo;
import arnnus.importationapi.repo.VinRepo;
import arnnus.importationapi.service.ImportateurService;
import dtos.VinListDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
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
        VinRepo vinRepoMock = mock(VinRepo.class); // Create a mock VinRepo
        ImportateurService importateurService = new ImportateurService(importateurRepoMock, vinRepoMock); // Pass the mock VinRepo to the constructor

        Page<Importateur> page = new PageImpl<>(Collections.singletonList(new Importateur()));
        when(importateurRepoMock.findAll(any(PageRequest.class))).thenReturn(page);

        assertEquals(page, importateurService.getAllImportateurs(0, 10));
    }

    @Test
    void testGetVinListForImportateur() {
        // Mock
        String importateurId = "1";
        VinList sampleVinList = new VinList();
        sampleVinList.setImportateurId(importateurId);
        List<VinList> vinList = Collections.singletonList(sampleVinList);

        // Mock
        VinRepo vinListRepoMock = mock(VinRepo.class);
        ImportateurRepo importateurRepoMock = mock(ImportateurRepo.class);
        ImportateurService importateurService = new ImportateurService(importateurRepoMock, vinListRepoMock);
        when(vinListRepoMock.findAllByImportateurId(importateurId)).thenReturn(vinList);

        List<VinListDto> result = importateurService.getVinListForImportateur(importateurId);

        // Assert
        assertEquals(vinList.size(), result.size());
        assertEquals(vinList.get(0).getImportateurId(), result.get(0).getImportateurId());
    }

    @Test
    void testCreateImportateur() {
        // Mock
        Importateur importateurToCreate = new Importateur();
        importateurToCreate.setId("1");
        importateurToCreate.setName("Sample Importateur");
        VinRepo vinRepoMock = mock(VinRepo.class);

        // Mock
        ImportateurRepo importateurRepoMock = mock(ImportateurRepo.class);
        ImportateurService importateurService = new ImportateurService(importateurRepoMock, vinRepoMock);
        when(importateurRepoMock.save(importateurToCreate)).thenReturn(importateurToCreate);

        Importateur result = importateurService.createImportateur(importateurToCreate);

        // Assert
        assertEquals(importateurToCreate, result);
    }

    @Test
    void testDeleteImportateur() {
        // Mock
        String importateurId = "1";
        ImportateurRepo importateurRepoMock = mock(ImportateurRepo.class);
        VinRepo vinRepoMock = mock(VinRepo.class);
        ImportateurService importateurService = new ImportateurService(importateurRepoMock, vinRepoMock);
        when(importateurRepoMock.findById(importateurId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> importateurService.deleteImportateur(importateurId));

        // Verify
        Mockito.verify(importateurRepoMock, Mockito.never()).deleteById(importateurId);
    }
}

