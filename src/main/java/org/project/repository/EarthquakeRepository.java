package org.project.repository;

import org.project.model.Earthquake;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EarthquakeRepository extends JpaRepository<Earthquake, Long> {

    List<Earthquake> findByMagnitudeGreaterThan(Double magnitude);

    List<Earthquake> findByTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
