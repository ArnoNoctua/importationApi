package arnnus.importationapi.tests;

import arnnus.importationapi.domain.Importateur;
import arnnus.importationapi.resource.ImportateurResource;
import arnnus.importationapi.service.ImportateurService;
import dtos.VinListDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ImportateurResourceTest {

    @Mock
    private ImportateurService importateurService;

    @InjectMocks
    private ImportateurResource importateurResource;

    @BeforeEach
    void setUp() {
        // Inject mock service
        ReflectionTestUtils.setField(importateurResource, "importateurService", importateurService);
    }

    @Test
    void testCreateImportateur() {
        ImportateurService importateurServiceMock = mock(ImportateurService.class);
        ImportateurResource importateurResource = new ImportateurResource(importateurServiceMock);

        Importateur sampleImportateur = new Importateur();
        sampleImportateur.setId(UUID.randomUUID().toString());
        sampleImportateur.setName("Sample Importateur");

        when(importateurServiceMock.createImportateur(any(Importateur.class))).thenReturn(sampleImportateur);

        ResponseEntity<Importateur> response = importateurResource.createImportateur(sampleImportateur);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Assert
        assertEquals(sampleImportateur, response.getBody());
        assertEquals(URI.create("/importateurs/userID"), response.getHeaders().getLocation());
    }

    @Test
    void testGetImportateurs() {
        ImportateurService importateurServiceMock = mock(ImportateurService.class);
        ImportateurResource importateurResource = new ImportateurResource(importateurServiceMock);

        Importateur importateur1 = new Importateur();
        Importateur importateur2 = new Importateur();
        List<Importateur> importateurs = new ArrayList<>();
        importateurs.add(importateur1);
        importateurs.add(importateur2);

        Page<Importateur> samplePage = Mockito.mock(Page.class);
        when(samplePage.getContent()).thenReturn(importateurs);

        // Mock
        when(importateurServiceMock.getAllImportateurs(0, 10)).thenReturn(samplePage);

        ResponseEntity<Page<Importateur>> response = importateurResource.getImportateurs(0, 10);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(importateurs, response.getBody().getContent());
    }

    @Test
    void testGetImportateurId() {
        String importateurId = UUID.randomUUID().toString();

        // Mock
        ImportateurService importateurServiceMock = mock(ImportateurService.class);
        ImportateurResource importateurResource = new ImportateurResource(importateurServiceMock);

        Importateur sampleImportateur = new Importateur();
        sampleImportateur.setId(importateurId);
        sampleImportateur.setName("Sample Importateur");

        // Mock
        Mockito.when(importateurServiceMock.getImportateur(importateurId)).thenReturn(sampleImportateur);

        ResponseEntity<Importateur> response = importateurResource.getImportateur(importateurId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleImportateur, response.getBody());
    }

    @Test
    void testUploadPhoto() throws IOException {
        String importateurId = "1";

        // Mock
        ImportateurService importateurServiceMock = mock(ImportateurService.class);
        ImportateurResource importateurResource = new ImportateurResource(importateurServiceMock);
        MultipartFile file = mock(MultipartFile.class);
        String samplePhotoUrl = "http://example.com/photo.jpg";

        // Mock
        when(importateurServiceMock.uploadPhoto(importateurId, file)).thenReturn(samplePhotoUrl);

        ResponseEntity<String> response = importateurResource.uploadPhoto(importateurId, file);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(samplePhotoUrl, response.getBody());
    }

    @Test
    void testDeleteImportateur() {
        String importateurId = "1";

        // Mock
        ImportateurService importateurServiceMock = mock(ImportateurService.class);
        ImportateurResource importateurResource = new ImportateurResource(importateurServiceMock);


        ResponseEntity<Void> response = importateurResource.deleteImportateur(importateurId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verify
        verify(importateurServiceMock).deleteImportateur(importateurId);
    }

    @Test
    void testGetVinListForImportateur() {
        String importateurId = "1";
        VinListDto vinListDto1 = new VinListDto();
        VinListDto vinListDto2 = new VinListDto();
        List<VinListDto> vinListDtos = new ArrayList<>();
        vinListDtos.add(vinListDto1);
        vinListDtos.add(vinListDto2);

        // Mock
        when(importateurService.getVinListForImportateur(importateurId)).thenReturn(vinListDtos);

        ResponseEntity<List<VinListDto>> response = importateurResource.getVinListForImportateur(importateurId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(vinListDtos, response.getBody());
    }

}