package uk.ac.sheffield.team28.team28.service;

import org.springframework.stereotype.Service;

import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberParticipation;
import uk.ac.sheffield.team28.team28.model.Music;
import uk.ac.sheffield.team28.team28.model.Performance;
import uk.ac.sheffield.team28.team28.repository.MemberParticipationRepository;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;
import uk.ac.sheffield.team28.team28.repository.MusicRepository;
import uk.ac.sheffield.team28.team28.repository.PerformanceRepository;

import java.util.List;

@Service
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final MemberParticipationRepository memberParticipationRepository;
    private final MemberRepository memberRepository;
     private final MusicRepository musicRepository;

    public PerformanceService(PerformanceRepository performanceRepository, MemberParticipationRepository memberParticipationRepository,  MemberRepository memberRepository, MusicRepository musicRepository) {
        this.performanceRepository = performanceRepository;
        this.memberParticipationRepository = memberParticipationRepository;
        this.memberRepository = memberRepository;
        this.musicRepository = musicRepository;
    }

    // Create a new performance
    public void createPerformance(Performance performance) {
        if (performance.getPlaylist() != null && !performance.getPlaylist().isEmpty()) {
            List<Long> musicIds = performance.getPlaylist().stream()
                                             .map(Music::getId)
                                             .toList(); // Extract IDs from the provided Music list
            List<Music> playlist = musicRepository.findAllById(musicIds);
            performance.setPlaylist(playlist);
        }

        // Save the performance to the repository
        performanceRepository.save(performance);
    }

    

    // Get all performances
    public List<Performance> getAllPerformances() {
        return performanceRepository.findAll();
    }

    public List<Member> getPlayersForPerformance(Long performanceId) {
        return performanceRepository.findById(performanceId)
                .map(Performance::getParticipations)
                .orElseThrow(() -> new RuntimeException("Performance not found"))
                .stream()
                .filter(MemberParticipation::isWillParticipate)
                .map(MemberParticipation::getMember)
                .toList();
    }

    // Get a specific performance by ID
    public Performance getPerformanceById(Long id) {
        return performanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Performance not found with id: " + id));
    }

    // Update a performance
    public Performance updatePerformance(Long id, Performance updatedPerformance) {
        Performance performance = getPerformanceById(id);

        performance.setVenue(updatedPerformance.getVenue());
        performance.setBand(updatedPerformance.getBand());
        performance.setDateTime(updatedPerformance.getDateTime());
        performance.setPlaylist(updatedPerformance.getPlaylist());

        return performanceRepository.save(performance);
    }

    // Delete a performance
    public void deletePerformance(Long id) {
        performanceRepository.deleteById(id);
    }

    // Indicate participation
    public void indicateParticipation(Long performanceId, Long memberId, boolean willParticipate) {
        MemberParticipation participation = memberParticipationRepository
                .findByPerformanceIdAndMemberId(performanceId, memberId)
                .orElse(new MemberParticipation());
    
        participation.setPerformance(getPerformanceById(performanceId));
        participation.setMember(memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId)));
        participation.setWillParticipate(willParticipate);
    
        memberParticipationRepository.save(participation);
    }
    
}
