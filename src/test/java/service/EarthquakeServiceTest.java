package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.Main;
import org.project.model.Earthquake;
import org.project.repository.EarthquakeRepository;
import org.project.service.EarthquakeService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
@DisplayName("Tests for EarthquakeService")
public class EarthquakeServiceTest {

    @Mock
    private EarthquakeRepository earthquakeRepository;

    @InjectMocks
    private EarthquakeService earthquakeService;

    Earthquake earthquake1;

    Earthquake earthquake2;

    @BeforeEach
    public void setUp() {
        earthquake1 = new Earthquake();
        earthquake1.setId(1L);
        earthquake1.setTitle("M 3.0 - 10km ENE of Pāhala, Hawaii");
        earthquake1.setTime(LocalDateTime.now());
        earthquake1.setMagnitude(3.0);
        earthquake1.setPlace("10km ENE of Pāhala, Hawaii");

        earthquake2 = new Earthquake();
        earthquake2.setId(2L);
        earthquake2.setTitle("M 4.0 - 15km SSE of Volcano, Hawaii");
        earthquake2.setTime(LocalDateTime.now());
        earthquake2.setMagnitude(4.0);
        earthquake2.setPlace("15km SSE of Volcano, Hawaii");

        earthquakeRepository.saveAll(Arrays.asList(earthquake1, earthquake2));
    }

    @Test
    public void testGetEarthquakesWithMagnitudeGreaterThan() {
        List<Earthquake> expectedEarthquakes = Arrays.asList(earthquake1, earthquake2);
        when(earthquakeRepository.findByMagnitudeGreaterThan(2.5)).thenReturn(expectedEarthquakes);

        List<Earthquake> actualEarthquakes = earthquakeService.getEarthquakesWithMagnitudeGreaterThan(2.5);

        assertEquals(expectedEarthquakes.size(), actualEarthquakes.size());
        assertEquals(expectedEarthquakes, actualEarthquakes);
        verify(earthquakeRepository, times(1)).findByMagnitudeGreaterThan(2.5);
    }

    @Test
    public void testGetEarthquakesBetweenTime() {
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now();

        List<Earthquake> expectedEarthquakes = Arrays.asList(earthquake1, earthquake2);
        when(earthquakeRepository.findByTimeBetween(startTime, endTime)).thenReturn(expectedEarthquakes);
        List<Earthquake> actualEarthquakes = earthquakeService.getEarthquakesBetweenTime(startTime, endTime);

        assertEquals(expectedEarthquakes.size(), actualEarthquakes.size());
        assertEquals(expectedEarthquakes, actualEarthquakes);
        verify(earthquakeRepository, times(1)).findByTimeBetween(startTime, endTime);
    }
}