package uk.ac.sheffield.team28.team28.repository;

import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.sheffield.team28.team28.model.Misc;

public interface MiscRepository extends JpaRepository<Misc, Long> {
}
