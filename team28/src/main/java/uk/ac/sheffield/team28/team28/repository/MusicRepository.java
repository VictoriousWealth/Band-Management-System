package uk.ac.sheffield.team28.team28.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.Music;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Long> {
    List<Music> findByBandInPracticeOrBandInPractice(BandInPractice band1, BandInPractice band2);
}
