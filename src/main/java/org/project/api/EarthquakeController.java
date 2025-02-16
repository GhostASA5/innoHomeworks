package org.project.api;

import lombok.RequiredArgsConstructor;
import org.project.dto.GeoJsonResponse;
import org.project.model.Earthquake;
import org.project.service.EarthquakeService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/api/earthquakes")
@RequiredArgsConstructor
public class EarthquakeController {

    private final EarthquakeService earthquakeService;

    @GetMapping("/magnitude")
    public List<Earthquake> getEarthquakesByMagnitude(@RequestParam Double magnitude) {
        return earthquakeService.getEarthquakesWithMagnitudeGreaterThan(magnitude);
    }

    @GetMapping("/time")
    public List<Earthquake> getEarthquakesByTime(@RequestParam String startTime, @RequestParam String endTime) {
        LocalDateTime start = LocalDateTime.parse(startTime);
        LocalDateTime end = LocalDateTime.parse(endTime);
        return earthquakeService.getEarthquakesBetweenTime(start, end);
    }

    @PostMapping("/import")
    public String importEarthquakes(@RequestBody GeoJsonResponse geoJsonResponse) {
        earthquakeService.loadEarthquakes(geoJsonResponse);
        return "Earthquakes imported successfully!";
    }
}
