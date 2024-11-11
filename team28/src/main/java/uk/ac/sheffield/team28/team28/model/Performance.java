package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Performances")
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Venue", nullable = false)
    private String venue;

    @Enumerated(EnumType.STRING)
    @Column(name = "Band", nullable = false)
    private BandInPractice band;

    @Column(name = "Date", nullable = false)
    private LocalDate date;

    // Constructors
    public Performance() {}

    public Performance(String venue, BandInPractice band, LocalDate date) {
        this.venue = venue;
        this.band = band;
        this.date = date;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getVenue() {
        return venue;
    }

    public BandInPractice getBand() {
        return band;
    }

    public LocalDate getDate() {
        return date;
    }
}
