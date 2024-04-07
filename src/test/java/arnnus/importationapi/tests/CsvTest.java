package arnnus.importationapi.tests;

import arnnus.importationapi.resource.CsvResource;
import arnnus.importationapi.service.CsvService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CsvTest {

    @Mock
    private CsvService csvService;

    private CsvResource csvResource;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        csvResource = new CsvResource(csvService);
    }

    @Test
    public void testUploadCSV() throws Exception {
        String importateurId = "testId";
        MultipartFile file = new MockMultipartFile("file", "Vins!".getBytes());
        List<Map<String, String>> parsedData = Collections.singletonList(Collections.singletonMap("key", "value"));

        when(csvService.parseCSV(file)).thenReturn(parsedData);

        ResponseEntity<?> response = csvResource.uploadCSV(file, importateurId);

        verify(csvService, times(1)).parseCSV(file);
        verify(csvService, times(1)).saveParsedDataToDatabase(importateurId, parsedData);
        assertEquals(ResponseEntity.ok(parsedData), response);
    }
}