package arnnus.importationapi.tests;

import arnnus.importationapi.domain.Importateur;
import arnnus.importationapi.resource.ImportateurResource;
import arnnus.importationapi.service.ImportateurService;
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
        // Inject mock service into resource
        ReflectionTestUtils.setField(importateurResource, "importateurService", importateurService);
    }

    @Test
    void testCreateImportateur() {
        // Create a mock ImportateurService
        ImportateurService importateurServiceMock = mock(ImportateurService.class);

        // Create an instance of ImportateurResource with the mocked service
        ImportateurResource importateurResource = new ImportateurResource(importateurServiceMock);

        // Define a sample Importateur object
        Importateur sampleImportateur = new Importateur();
        sampleImportateur.setId(UUID.randomUUID().toString());
        sampleImportateur.setName("Sample Importateur");

        // Mock the behavior of importateurService.createImportateur() to return the sample Importateur object
        when(importateurServiceMock.createImportateur(any(Importateur.class))).thenReturn(sampleImportateur);

        // Call the createImportateur() method of ImportateurResource with a sample Importateur
        ResponseEntity<Importateur> response = importateurResource.createImportateur(sampleImportateur);

        // Assert that the response status code is CREATED (201)
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Assert that the returned Importateur object matches the sample Importateur
        assertEquals(sampleImportateur, response.getBody());

        // Assert that the Location header in the response contains the correct URI
        assertEquals(URI.create("/importateurs/userID"), response.getHeaders().getLocation());
    }

    @Test
    void testGetImportateurs() {
        // Create a mock ImportateurService
        ImportateurService importateurServiceMock = mock(ImportateurService.class);

        // Create an instance of ImportateurResource with the mocked service
        ImportateurResource importateurResource = new ImportateurResource(importateurServiceMock);

        // Define sample Importateur objects
        Importateur importateur1 = new Importateur();
        Importateur importateur2 = new Importateur();
        List<Importateur> importateurs = new ArrayList<>();
        importateurs.add(importateur1);
        importateurs.add(importateur2);

        // Create a sample Page object
        Page<Importateur> samplePage = Mockito.mock(Page.class);
        when(samplePage.getContent()).thenReturn(importateurs);

        // Mock the behavior of importateurService.getAllImportateurs() to return the sample Page object
        when(importateurServiceMock.getAllImportateurs(0, 10)).thenReturn(samplePage);

        // Call the getImportateurs() method of ImportateurResource
        ResponseEntity<Page<Importateur>> response = importateurResource.getImportateurs(0, 10);

        // Assert that the response status code is OK (200)
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert that the returned Page object matches the sample Page object
        assertEquals(importateurs, response.getBody().getContent());
    }

    @Test
    void testGetImportateurId() {
        // Generate a random UUID for importateurId
        String importateurId = UUID.randomUUID().toString();

        // Mock the ImportateurService
        ImportateurService importateurServiceMock = mock(ImportateurService.class);

        // Create an instance of ImportateurResource with the mocked service
        ImportateurResource importateurResource = new ImportateurResource(importateurServiceMock);

        // Define a sample Importateur object
        Importateur sampleImportateur = new Importateur();
        sampleImportateur.setId(importateurId);
        sampleImportateur.setName("Sample Importateur");

        // Mock the behavior of importateurService.getImportateur() to return the sample Importateur object
        Mockito.when(importateurServiceMock.getImportateur(importateurId)).thenReturn(sampleImportateur);

        // Call the getImportateur() method of ImportateurResource
        ResponseEntity<Importateur> response = importateurResource.getImportateur(importateurId);

        // Assert that the response status code is OK (200)
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert that the returned Importateur object matches the sample Importateur
        assertEquals(sampleImportateur, response.getBody());
    }

    @Test
    void testUploadPhoto() throws IOException {
        // Define a sample importateur ID
        String importateurId = "1";

        // Create a mock ImportateurService
        ImportateurService importateurServiceMock = mock(ImportateurService.class);

        // Create an instance of ImportateurResource with the mocked service
        ImportateurResource importateurResource = new ImportateurResource(importateurServiceMock);

        // Define a sample MultipartFile object
        MultipartFile file = mock(MultipartFile.class);

        // Define a sample photo URL
        String samplePhotoUrl = "http://example.com/photo.jpg";

        // Mock the behavior of importateurService.uploadPhoto() to return the sample photo URL
        when(importateurServiceMock.uploadPhoto(importateurId, file)).thenReturn(samplePhotoUrl);

        // Call the uploadPhoto() method of ImportateurResource
        ResponseEntity<String> response = importateurResource.uploadPhoto(importateurId, file);

        // Assert that the response status code is OK (200)
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert that the returned photo URL matches the sample photo URL
        assertEquals(samplePhotoUrl, response.getBody());
    }

    @Test
    void testDeleteImportateur() {
        // Define a sample importateur ID
        String importateurId = "1";

        // Create a mock ImportateurService
        ImportateurService importateurServiceMock = mock(ImportateurService.class);

        // Create an instance of ImportateurResource with the mocked service
        ImportateurResource importateurResource = new ImportateurResource(importateurServiceMock);

        // Call the deleteImportateur() method of ImportateurResource
        ResponseEntity<Void> response = importateurResource.deleteImportateur(importateurId);

        // Assert that the response status code is NO_CONTENT (204)
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verify that the deleteImportateur method of ImportateurService is called with the correct importateurId
        verify(importateurServiceMock).deleteImportateur(importateurId);
    }

}