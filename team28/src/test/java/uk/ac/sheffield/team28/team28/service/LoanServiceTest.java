package uk.ac.sheffield.team28.team28.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.model.*;
import uk.ac.sheffield.team28.team28.repository.InstrumentRepository;
import uk.ac.sheffield.team28.team28.repository.LoanRepository;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Service
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private InstrumentRepository instrumentRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private LoanService loanService;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldFindLoanById() {
        Member member1 = new Member(29L, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        Item item1 = new Item(ItemType.Instrument, "item1", "make", "note");
        Loan loan1 = new Loan(member1, item1, LocalDate.now(),null);
        Long loanId = loan1.getId();
        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan1));

        Optional<Loan> loan = loanService.findLoanById(loanId);
        if (loan.isPresent()) {
            assertEquals(loan.get(), loan1);
        } else {
            fail();
        }
    }

    @Test
    public void shouldFindAllLoansByMemberId() {
        List<Loan> loans = new ArrayList<>();;
        Member member1 = new Member(29L, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        Item item1 = new Item(ItemType.Instrument, "item1", "make", "note");
        Item item2 = new Item(ItemType.Instrument, "item2", "make", "note");
        Loan loan1 = new Loan(member1, item1, LocalDate.now(),null);
        Loan loan2 = new Loan(member1, item2, LocalDate.now(),null);
        loans.add(loan1);
        loans.add(loan2);
        when(loanRepository.findByMemberId(member1.getId())).thenReturn(loans);

        List<Loan> serviceLoans = loanService.getLoansByMemberId(member1.getId());
        assertEquals(serviceLoans.size(), 2);
        assertEquals(serviceLoans.get(0), loan1);
        assertEquals(serviceLoans.get(1), loan2);
    }

    @Test
    public void shouldReturnAllActiveLoansByMemberId() {
        Member member1 = new Member(29L, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        Item item1 = new Item(ItemType.Instrument, "item1", "make", "note");
        Loan loan1 = new Loan(member1, item1, LocalDate.now(),null);
        Loan loan2 = new Loan(member1, item1, LocalDate.now(),LocalDate.now());
        Long memberId = member1.getId();
        when(loanRepository.findByMemberId(memberId)).thenReturn(Arrays.asList(loan1, loan2));

        List<Loan> activeLoans = loanService.getActiveLoansByMemberId(memberId);
        assertEquals(1, activeLoans.size());
        assertEquals("item1", activeLoans.get(0).getItem().getNameTypeOrTitle());
    }

    @Test
    public void shouldFindActiveLoanByItemId() {

        Member member = new Member(29L, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        Item item = new Item(ItemType.Instrument, "item1", "make", "note");
        Loan activeLoan = new Loan(member, item, LocalDate.now(), null); // Active loan (no return date)
        Loan returnedLoan = new Loan(member, item, LocalDate.now().minusDays(10), LocalDate.now()); // Returned loan
        List<Loan> loans = Arrays.asList(activeLoan, returnedLoan);

        Long itemId = item.getId();

        when(loanRepository.findByItemId(itemId)).thenReturn(loans);

        Loan result = loanService.findActiveLoanByItemId(itemId);
        assertEquals(activeLoan, result);
    }
}
