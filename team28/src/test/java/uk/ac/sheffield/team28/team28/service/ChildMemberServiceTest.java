package uk.ac.sheffield.team28.team28.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.repository.ChildMemberRepository;
import uk.ac.sheffield.team28.team28.repository.LoanRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ChildMemberServiceTest {

    @Mock
    private ChildMemberRepository childMemberRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private ChildMemberService childMemberService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testGetChildByFullName() {
        String fullName = "john doe";
        String capitalizedFullName = "John Doe";

        ChildMember childMember = new ChildMember();
        when(childMemberRepository.findByName(capitalizedFullName)).thenReturn(Optional.of(childMember));

        Optional<ChildMember> result = childMemberService.getChildByFullName(fullName);

        assertTrue(result.isPresent());
        assertEquals(childMember, result.get());
        verify(childMemberRepository, times(1)).findByName(capitalizedFullName);
    }

    @Test
    void testGetChildByParent() {
        Member parent = new Member();
        List<ChildMember> children = Arrays.asList(new ChildMember(), new ChildMember());
        when(childMemberRepository.findAllByParent(parent)).thenReturn(children);

        List<ChildMember> result = childMemberService.getChildByParent(parent);

        assertEquals(children, result);
        verify(childMemberRepository, times(1)).findAllByParent(parent);
    }

    @Test
    void testGetChildById() throws Exception {
        Long childId = 1L;
        ChildMember childMember = new ChildMember();
        when(childMemberRepository.findById(childId)).thenReturn(Optional.of(childMember));

        ChildMember result = childMemberService.getChildById(childId);

        assertEquals(childMember, result);
        verify(childMemberRepository, times(1)).findById(childId);
    }

    @Test
    void testGetAllChildren() {
        List<ChildMember> children = Arrays.asList(new ChildMember(), new ChildMember());
        when(childMemberRepository.findAllChildren()).thenReturn(children);

        List<ChildMember> result = childMemberService.getAllChildren();

        assertEquals(children, result);
        verify(childMemberRepository, times(1)).findAllChildren();
    }

    @Test
    void testGetAllParents() {
        List<Member> parents = Arrays.asList(new Member(), new Member());
        when(childMemberRepository.findAllParents()).thenReturn(parents);

        List<Member> result = childMemberService.getAllParents();

        assertEquals(parents, result);
        verify(childMemberRepository, times(1)).findAllParents();
    }

    @Test
    void testDoAnyChildrenHaveLoans() {
        Member parent = new Member();
        ChildMember child = new ChildMember();
        child.setId(1L);
        List<ChildMember> children = List.of(child);

        when(childMemberRepository.findAllByParent(parent)).thenReturn(children);
        when(loanRepository.childHasActiveLoans(1L)).thenReturn(true);

        boolean result = childMemberService.doAnyChildrenHaveLoans(parent);

        assertTrue(result);
        verify(loanRepository, times(1)).childHasActiveLoans(1L);
    }

    @Test
    void testAddChildMemberToBand() throws Exception {
        Long childId = 1L;
        ChildMember child = new ChildMember();
        child.setBand(BandInPractice.None);

        when(childMemberRepository.findById(childId)).thenReturn(Optional.of(child));

        ChildMember result = childMemberService.addChildMemberToBand(childId);

        assertEquals(BandInPractice.Training, result.getBand());
        verify(childMemberRepository, times(1)).save(child);
    }

    @Test
    void testRemoveChildMemberFromBand() throws Exception {
        Long childId = 1L;
        ChildMember child = new ChildMember();
        child.setBand(BandInPractice.Training);

        when(childMemberRepository.findById(childId)).thenReturn(Optional.of(child));

        ChildMember result = childMemberService.removeChildMemberFromBand(childId);

        assertEquals(BandInPractice.None, result.getBand());
        verify(childMemberRepository, times(1)).save(child);
    }

    @Test
    void testDeleteChildMembers() throws Exception {
        Member parent = new Member();
        ChildMember child1 = new ChildMember();
        child1.setId(1L);
        ChildMember child2 = new ChildMember();
        child2.setId(2L);
        List<ChildMember> children = Arrays.asList(child1, child2);

        when(childMemberRepository.findAllByParent(parent)).thenReturn(children);

        childMemberService.deleteChildMembers(parent);

        verify(loanRepository, times(1)).deleteLoansByChildMemberId(1L);
        verify(loanRepository, times(1)).deleteLoansByChildMemberId(2L);
        verify(childMemberRepository, times(1)).deleteById(1L);
        verify(childMemberRepository, times(1)).deleteById(2L);
    }

    @Test
    void testSaveChildMember() {
        ChildMember child = new ChildMember();

        childMemberService.save(child);

        verify(childMemberRepository, times(1)).save(child);
    }


}


