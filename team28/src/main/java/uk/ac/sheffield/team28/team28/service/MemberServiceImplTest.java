//package uk.ac.sheffield.team28.team28.service;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import uk.ac.sheffield.team28.team28.model.BandInPractice;
//import uk.ac.sheffield.team28.team28.model.Member;
//import uk.ac.sheffield.team28.team28.model.MemberType;
//import uk.ac.sheffield.team28.team28.repository.MemberRepository;
//
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//
//public class MemberServiceImplTest {
//
//    @Mock
//    private MemberRepository memberRepository;
////    @InjectMocks
////    private MemberServiceImplTest memberService;
//
//    @InjectMocks
//    private MemberService memberService;
//
//    private final PasswordEncoder passwordEncoder;
//
//    public MemberServiceImplTest(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }
//
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void shouldReturnAllMembers_whenGetAllMembersIsCalled() {
//        Member member1 = new Member(29L, "a@z.com","password",MemberType.Adult,"090378734","John","Doe");
//        Member member2 = new Member(30L, "a@y.com","password",MemberType.Adult,"090376734","Johnny","Deoe");
//        Member member3 = new Member(31L, "a@x.com","password",MemberType.Adult,"090375734","Jon","Doey");
//        when(memberRepository.findAll()).thenReturn(Arrays.asList(member1, member2, member3));
//
//        List<Member> members = memberService.getAllMembers();
//        assertEquals(3, members.size());
//        assertEquals("John", members.get(0).getFirstName());
//        assertEquals("Johnny", members.get(1).getFirstName());
//        assertEquals("Jon", members.get(2).getFirstName());
//
//    }
//
//    @Test
//    public void shouldPromoteMember_whenPromoteMemberWithIdCalled() {
//        Member member1 = new Member(29L, "a@z.com","password",MemberType.Adult,"090378734","John","Doe");
//        Long memberId = 29L;
//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member1));
//        try {
//            Member member = memberService.promoteMemberWithId(memberId);
//            assertEquals(MemberType.Committee, member.getMemberType());
//
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    @Test
//    public void shouldPromoteAdultMemberToCommittee() throws Exception {
//        Long memberId = 29L;
//        Member member = new Member(memberId, "a@z.com", "password", MemberType.Adult, "090378734", "John", "Doe");
//
//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//        when(memberRepository.save(member)).thenReturn(member);
//
//        Member updatedMember = memberService.promoteMemberWithId(memberId);
//
//        assertEquals(MemberType.Committee, updatedMember.getMemberType());
//        verify(memberRepository).save(member);
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenPromotingNonAdultMember() {
//        Long memberId = 29L;
//        Member member = new Member(memberId, "a@z.com", "password", MemberType.Committee, "090378734", "John", "Doe");
//
//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//
//        Exception exception = assertThrows(Exception.class, () -> {
//            memberService.promoteMemberWithId(memberId);
//        });
//
//        assertEquals("Member cannot be added.", exception.getMessage());
//        verify(memberRepository, never()).save(member);
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenMemberNotFoundForPromotion() {
//        Long memberId = 29L;
//
//        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(Exception.class, () -> {
//            memberService.promoteMemberWithId(memberId);
//        });
//
//        assertEquals("Member not found with ID: " + memberId, exception.getMessage());
//        verify(memberRepository, never()).save(any(Member.class));
//    }
//
//
//    @Test
//    public void shouldDemoteCommitteeMemberToAdult() throws Exception {
//        Long memberId = 29L;
//        Member member = new Member(memberId, "a@z.com", "password", MemberType.Committee, "090378734", "John", "Doe");
//
//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//        when(memberRepository.save(member)).thenReturn(member);
//
//        Member updatedMember = memberService.demoteMemberWithId(memberId);
//
//        assertEquals(MemberType.Adult, updatedMember.getMemberType());
//        verify(memberRepository).save(member);
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenDemotingNonCommitteeMember() {
//        Long memberId = 29L;
//        Member member = new Member(memberId, "a@z.com", "password", MemberType.Adult, "090378734", "John", "Doe");
//
//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//
//        Exception exception = assertThrows(Exception.class, () -> {
//            memberService.demoteMemberWithId(memberId);
//        });
//
//        assertEquals("Member cannot be demoted.", exception.getMessage());
//        verify(memberRepository, never()).save(member);
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenMemberNotFoundForDemotion() {
//        Long memberId = 29L;
//
//        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(Exception.class, () -> {
//            memberService.demoteMemberWithId(memberId);
//        });
//
//        assertEquals("Member not found with ID: " + memberId, exception.getMessage());
//        verify(memberRepository, never()).save(any(Member.class));
//    }
//
//
//    @Test
//    public void shouldAddMemberToNewBand_whenMemberIsInNoneBand() throws Exception {
//        Long memberId = 29L;
//        Member member = new Member(memberId, "a@z.com", "password", MemberType.Adult, "090378734", "John", "Doe");
//        member.setBand(BandInPractice.None);
//
//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//        when(memberRepository.save(member)).thenReturn(member);
//
//        Member updatedMember = memberService.addMemberToBand(memberId, BandInPractice.Training);
//
//        assertEquals(BandInPractice.Training, updatedMember.getBand());
//    }
//
//    @Test
//    public void shouldUpdateBandToBoth_whenMemberIsAlreadyInOneBand() throws Exception {
//        Long memberId = 29L;
//        Member member = new Member(memberId, "a@z.com", "password", MemberType.Adult, "090378734", "John", "Doe");
//        member.setBand(BandInPractice.Training);
//
//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//        when(memberRepository.save(member)).thenReturn(member);
//
//        Member updatedMember = memberService.addMemberToBand(memberId, BandInPractice.Senior);
//
//        assertEquals(BandInPractice.Both, updatedMember.getBand());
//    }
//
//    @Test
//    public void shouldThrowException_whenAddingMemberToSameBand() {
//        Long memberId = 29L;
//        Member member = new Member(memberId, "a@z.com", "password", MemberType.Adult, "090378734", "John", "Doe");
//        member.setBand(BandInPractice.Training);
//
//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//
//        Exception exception = assertThrows(Exception.class, () -> {
//            memberService.addMemberToBand(memberId, BandInPractice.Training);
//        });
//
//        assertEquals("Member is already in this band.", exception.getMessage());
//    }
//
//    @Test
//    public void shouldRemoveMemberFromBand_whenMemberIsInSpecifiedBand() throws Exception {
//        Long memberId = 29L;
//        Member member = new Member(memberId, "a@z.com", "password", MemberType.Adult, "090378734", "John", "Doe");
//        member.setBand(BandInPractice.Training);
//
//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//        when(memberRepository.save(member)).thenReturn(member);
//
//        Member updatedMember = memberService.removeMemberFromBand(memberId, BandInPractice.Training);
//
//        assertEquals(BandInPractice.None, updatedMember.getBand());
//    }
//
//    @Test
//    public void shouldUpdateBandToSingleBand_whenMemberIsInBothBands() throws Exception {
//        Long memberId = 29L;
//        Member member = new Member(memberId, "a@z.com", "password", MemberType.Adult, "090378734", "John", "Doe");
//        member.setBand(BandInPractice.Both);
//
//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//        when(memberRepository.save(member)).thenReturn(member);
//
//        Member updatedMember = memberService.removeMemberFromBand(memberId, BandInPractice.Training);
//
//        assertEquals(BandInPractice.Senior, updatedMember.getBand());
//    }
//
//    @Test
//    public void shouldReturnCommitteeMembers_whenGetCommitteeMembersIsCalled() {
//        Member member1 = new Member(29L, "a@z.com", "password", MemberType.Committee, "090378734", "John", "Doe");
//        Member member2 = new Member(30L, "a@y.com", "password", MemberType.Committee, "090376734", "Jane", "Doe");
//
//        when(memberRepository.findByMemberType(MemberType.Committee)).thenReturn(Arrays.asList(member1, member2));
//
//        List<Member> committeeMembers = memberService.getCommitteeMembers();
//
//        assertEquals(2, committeeMembers.size());
//        assertEquals("John", committeeMembers.get(0).getFirstName());
//        assertEquals("Jane", committeeMembers.get(1).getFirstName());
//    }
//
//    @Test
//    public void shouldAuthoriseMember_whenCredentialsMatch() throws Exception {
//        Long memberId = 29L;
//        String rawPassword = "password";
//        Member member = new Member(memberId, "a@z.com", "$2a$10$hashedpassword", MemberType.Adult, "090378734", "John", "Doe");
//
//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//        //String passwordEncoder;
//        when(passwordEncoder.matches(rawPassword, member.getPassword())).thenReturn(true);
//
//        boolean isAuthorised = memberService.authorise(memberId, rawPassword);
//
//        assertTrue(isAuthorised);
//    }
//
//    @Test
//    public void shouldNotAuthoriseMember_whenCredentialsDoNotMatch() throws Exception {
//        Long memberId = 29L;
//        String rawPassword = "wrongPassword";
//        Member member = new Member(memberId, "a@z.com", "$2a$10$hashedpassword", MemberType.Adult, "090378734", "John", "Doe");
//
//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//        when(passwordEncoder.matches(rawPassword, member.getPassword())).thenReturn(false);
//
//        boolean isAuthorised = memberService.authorise(memberId, rawPassword);
//
//        assertFalse(isAuthorised);
//    }
//
//
//
//}
