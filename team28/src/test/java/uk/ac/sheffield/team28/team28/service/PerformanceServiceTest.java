package uk.ac.sheffield.team28.team28.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.dto.MemberRegistrationDto;
import uk.ac.sheffield.team28.team28.dto.MusicDto;
import uk.ac.sheffield.team28.team28.dto.OrderDto;
import uk.ac.sheffield.team28.team28.model.*;
import uk.ac.sheffield.team28.team28.repository.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PerformanceServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MusicRepository musicRepository;

    @Mock
    private PerformanceRepository performanceRepository;

    @Mock
    private MemberParticipationRepository memberParticipationRepository;
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PerformanceService performanceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePerformance_withPlaylist() {
        // Make performance
        Music music1 = new Music();
        music1.setId(29L);
        Music music2 = new Music();
        music1.setId(30L);

        Performance performance = new Performance();
        performance.setPlaylist(List.of(music1, music2));
        //Mock the repo response
        when(musicRepository.findAllById(List.of(29L, 30L))).thenReturn(List.of(music1, music2));
        //Create the performance
        performanceService.createPerformance(performance); //Turns the list to 0
        // Check the result
        verify(performanceRepository, times(1)).save(performance);
        assertEquals(2, performance.getPlaylist().size());
    }

    @Test
    void testCreatePerformance_withoutPlaylist() {
        //Make the performance
        Performance performance = new Performance();
        performance.setPlaylist(null); //No music
        // Make performance
        performanceService.createPerformance(performance);
        // Check result
        verify(performanceRepository, times(1)).save(performance);
        assertNull(performance.getPlaylist());
    }

    @Test
    void getAllPerformances() {
        // Make performances
        Performance performance1 = new Performance();
        Performance performance2 = new Performance();
        //Mock the repo response
        when(performanceRepository.findAll()).thenReturn(List.of(performance1, performance2));
        // Get the performances
        List<Performance> performances = performanceService.getAllPerformances();
        // Check the result
        assertEquals(2, performances.size());
        verify(performanceRepository, times(1)).findAll();
    }

    @Test
    void testGetPlayersForPerformance() {
        // Make participants
        Member member = new Member();
        MemberParticipation participation = new MemberParticipation();
        participation.setMember(member);
        participation.setWillParticipate(true);
        //Make a performance
        Performance performance = new Performance();
        performance.setParticipations(List.of(participation));
        //Mock the repo response
        when(performanceRepository.findById(1L)).thenReturn(Optional.of(performance));
        //Get the performance players
        List<Member> players = performanceService.getPlayersForPerformance(1L);
        // Check the result
        assertEquals(1, players.size());
        verify(performanceRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPerformanceById_ASucces() {
        // Make performance
        Performance performance = new Performance();
        //Moch the repo response
        when(performanceRepository.findById(1L)).thenReturn(Optional.of(performance));
        // Get the result
        Performance result = performanceService.getPerformanceById(1L);
        // Check the result
        assertNotNull(result);
        verify(performanceRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPerformanceById_doesntExist() {
        // Mock the repo response
        when(performanceRepository.findById(1L)).thenReturn(Optional.empty());
        //Get exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            performanceService.getPerformanceById(1L);
        });
        //Check its right
        assertEquals("Performance not found with id: " + 1L,exception.getMessage());
    }

    @Test
    void testUpdatePerformance() {
        // Make a performance
        Performance performance = new Performance();
        performance.setVenue("Batcave");
        //Update it
        performance.setVenue("Fortress of solitude");
        //Mock the repo responses
        when(performanceRepository.findById(1L)).thenReturn(Optional.of(performance));
        when(performanceRepository.save(performance)).thenReturn(performance);
        // Update the performance
        Performance result = performanceService.updatePerformance(1L, performance);
        // check result
        assertEquals("Fortress of solitude", result.getVenue());
        verify(performanceRepository, times(1)).save(performance);
    }

    @Test
    void testDeletePerformance() {
        // Delete the performance
        performanceService.deletePerformance(1L);
        // Check its deleted
        verify(performanceRepository, times(1)).deleteById(1L);
    }

    @Test
    void testIndicateParticipation_newParticipation() {
        // Make performance
        Performance performance = new Performance();
        Member member = new Member();
        //Mock repo responses
        when(performanceRepository.findById(1L)).thenReturn(Optional.of(performance));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        //Indicate participation
        performanceService.indicateParticipation(1L, 1L, true);
        //Check result
        verify(memberParticipationRepository, times(1)).save(any(MemberParticipation.class));
    }

    @Test
    void testIndicateParticipation_existingParticipation() {
        // Make performance
        Performance performance = new Performance();
        Member member = new Member();
        MemberParticipation existingParticipation = new MemberParticipation();
        //Mock repo responses
        when(performanceRepository.findById(1L)).thenReturn(Optional.of(performance));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberParticipationRepository.findByPerformanceIdAndMemberId(1L, 1L))
                .thenReturn(Optional.of(existingParticipation));
        // Indicate participation
        performanceService.indicateParticipation(1L, 1L, true);
        // Check result
        verify(memberParticipationRepository, times(1)).save(existingParticipation);
    }
}