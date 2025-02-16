package org.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.dto.GeoJsonResponse;
import org.project.dto.Feature;
import org.project.dto.Properties;
import org.project.model.Earthquake;
import org.project.repository.EarthquakeRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EarthquakeService {

    private final EarthquakeRepository earthquakeRepository;

    public List<Earthquake> getEarthquakesWithMagnitudeGreaterThan(Double magnitude) {
        return earthquakeRepository.findByMagnitudeGreaterThan(magnitude);
    }

    public List<Earthquake> getEarthquakesBetweenTime(LocalDateTime startTime, LocalDateTime endTime) {
        return earthquakeRepository.findByTimeBetween(startTime, endTime);
    }

    public void loadEarthquakes(GeoJsonResponse geoJsonResponse) {
        List<Feature> features = geoJsonResponse.getFeatures();

        for (Feature feature : features) {
            Properties properties = feature.getProperties();

            Earthquake earthquake = new Earthquake();
            earthquake.setTitle(properties.getTitle());
            earthquake.setTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(properties.getTime()), ZoneId.systemDefault()));
            earthquake.setMagnitude(properties.getMag());
            earthquake.setPlace(properties.getPlace());

            earthquakeRepository.save(earthquake);
        }
    }
}
