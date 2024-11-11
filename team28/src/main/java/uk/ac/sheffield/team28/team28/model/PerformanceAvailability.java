package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;

@Entity
@Table(name = "PerformanceAvailability")
public class PerformanceAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance;

    //Constructors
    public PerformanceAvailability() {}

    public PerformanceAvailability(Member member, Performance performance) {
        this.member = member;
        this.performance = performance;
    }


    // Getters
    public Long getId() {
        return id;
    }

    public Member getMemberId() {
        return member;
    }

    public Performance getPerformance() {
        return performance;
    }
}
