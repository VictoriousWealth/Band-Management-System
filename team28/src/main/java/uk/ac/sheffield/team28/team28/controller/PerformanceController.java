package uk.ac.sheffield.team28.team28.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.Music;
import uk.ac.sheffield.team28.team28.model.Performance;
import uk.ac.sheffield.team28.team28.service.PerformanceService;

import java.util.List;

@RestController
@RequestMapping("/performances")
public class PerformanceController {

    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    // Create a new performance
    @PostMapping
    public ResponseEntity<String> createPerformance(@RequestBody Performance performance) {
        System.out.println("Creating performance: " + performance);
        try {
            performanceService.createPerformance(performance);
            return ResponseEntity.ok("Performance created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create performance.");
        }
    }

    // Get all performances
    @GetMapping
    public List<Performance> getAllPerformances() {
        List<Performance> performances = performanceService.getAllPerformances();
        System.out.println("Performances fetched: " + performances);
        return performances;
    }

    @GetMapping("/{id}/players")
    public ResponseEntity<List<Member>> getPlayersForPerformance(@PathVariable Long id) {
        List<Member> players = performanceService.getPlayersForPerformance(id);
        return ResponseEntity.ok(players);
    }

    // Get a specific performance by ID
    @GetMapping("/{id}")
    public ResponseEntity<Performance> getPerformanceById(@PathVariable Long id) {
        Performance performance = performanceService.getPerformanceById(id);
        return ResponseEntity.ok(performance);
    }

    // Update a performance
    @PutMapping("/{id}")
    public ResponseEntity<Performance> updatePerformance(@PathVariable Long id, @RequestBody Performance updatedPerformance) {
        Performance performance = performanceService.updatePerformance(id, updatedPerformance);
        return ResponseEntity.ok(performance);
    }

    // Delete a performance
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformance(@PathVariable Long id) {
        performanceService.deletePerformance(id);
        return ResponseEntity.noContent().build();
    }

    

    // Indicate participation for a member
    @PostMapping("/{performanceId}/participation")
    public ResponseEntity<String> indicateParticipation(@PathVariable Long performanceId, @RequestParam Long memberId, @RequestParam boolean willParticipate) {
        performanceService.indicateParticipation(performanceId, memberId, willParticipate);
        return ResponseEntity.ok("Participation updated successfully.");
    }
}
