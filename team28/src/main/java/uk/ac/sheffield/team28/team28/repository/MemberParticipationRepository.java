package uk.ac.sheffield.team28.team28.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.sheffield.team28.team28.model.MemberParticipation;

import java.util.Optional;

@Repository
public interface MemberParticipationRepository extends JpaRepository<MemberParticipation, Long> {
    Optional<MemberParticipation> findByPerformanceIdAndMemberId(Long performanceId, Long memberId);
}
