package uk.ac.sheffield.team28.team28.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.model.Instrument;
import uk.ac.sheffield.team28.team28.model.Item;
import uk.ac.sheffield.team28.team28.model.Loan;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.repository.InstrumentRepository;
import uk.ac.sheffield.team28.team28.repository.LoanRepository;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LoanService {


    private final LoanRepository loanRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private InstrumentRepository instrumentRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    // Finds a loan by ID
    public Optional<Loan> findLoanById(Long id) {
        return loanRepository.findById(id);
    }

    // Find all loans for a particular member
    public List<Loan> getLoansByMemberId(Long memberId) {
        return loanRepository.findByMemberId(memberId);
    }

    public List<Loan> getLoansByChildMemberId(Long memberId) {
        return loanRepository.findByChildMemberId(memberId);
    }

    public List<Loan> getActiveLoansByMemberId(Long memberId) {
        List<Loan> allLoans = this.getLoansByMemberId(memberId);
        return getActiveLoans(allLoans);
    }

    public List<Loan> getActiveLoansByChildMemberId(Long childMemberId) {
        List<Loan> allLoans = this.getLoansByMemberId(childMemberId);
        return getActiveLoans(allLoans);
    }

    public List<Loan> getActiveLoans(List<Loan> allLoans){
        List<Loan> activeLoans = new ArrayList<>();
        for (Loan loan : allLoans) {
            if (loan.getReturnDate() == null) {
                activeLoans.add(loan);
            }
        }
        return activeLoans;
    }

    // Find active loan for a particular item using its id
    public Loan findActiveLoanByItemId(Long itemId) {
        List<Loan> loans = loanRepository.findByItemId(itemId);
        return loans.stream()
                .filter(loan -> loan.getReturnDate() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No active loan found for item ID: " + itemId));
    }

    // Retrieves all loans
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    // Delete a loan by ID
    public void deleteLoan(Long id) {
        loanRepository.deleteById(id);
    }

    public void loanInstrument(Long instrumentId, String memberName) {
        // Find member by full name
        Member member = memberService.findMemberByFullName(memberName);
        if (member == null) {
            throw new IllegalArgumentException("No member found with that name.");
        }

        // Check if instrument is already on loan
        Instrument instrument = instrumentRepository.findById(instrumentId)
                .orElseThrow(() -> new NoSuchElementException("Instrument not found"));

        if (!instrument.getItem().getInStorage()) {
            throw new IllegalStateException("Instrument is already on loan");
        }

        // Create loan
        Loan loan = new Loan();
        loan.setMember(member);
        loan.setItem(instrument.getItem());
        loan.setLoanDate(LocalDate.now());
        loan.setReturnDate(null);
        loanRepository.save(loan);

        // Update instrument status
        instrument.getItem().setInStorage(false);
        instrumentRepository.save(instrument);
    }

    public void returnInstrument(Long instrumentId) {
        // Fetch the instrument and its associated item
        Instrument instrument = instrumentRepository.findById(instrumentId)
                .orElseThrow(() -> new NoSuchElementException("Instrument not found"));
        Item item = instrument.getItem();

        // Check if the item is currently on loan
        if (item.getInStorage()) {
            throw new IllegalStateException("Instrument is not currently on loan");
        }

        // Find the active loan for the item
        Loan loan = this.findActiveLoanByItemId(item.getId());

        // Mark the loan as completed (return date set)
        loan.setReturnDate(LocalDate.now());
        loanRepository.save(loan);

        // Update the item's in_storage status
        item.setInStorage(true);
        instrumentRepository.save(instrument);
    }
}
