package uk.ac.sheffield.team28.team28.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.sheffield.team28.team28.model.PerformanceAvailability;

@Repository
public interface PerformanceAvailabilityRepository extends JpaRepository<PerformanceAvailability, Long> {
}