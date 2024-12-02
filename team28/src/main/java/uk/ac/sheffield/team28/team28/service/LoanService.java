package uk.ac.sheffield.team28.team28.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.model.Instrument;
import uk.ac.sheffield.team28.team28.model.Loan;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.repository.InstrumentRepository;
import uk.ac.sheffield.team28.team28.repository.LoanRepository;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;

import java.time.LocalDate;
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



    public Loan createLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    // Finds a loan by ID
    public Optional<Loan> findLoanById(Long id) {
        return loanRepository.findById(id);
    }

    // Find all loans for a particular member
    public List<Loan> getLoansByMemberId(Long memberId) {
        return loanRepository.findByMemberId(memberId);
    }

    // Retrieves all loans
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Loan updateLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    // Delete a loan by ID
    public void deleteLoan(Long id) {
        loanRepository.deleteById(id);
    }

    public Loan save(Loan loan) {
        return loan;
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
        instrument.getItem().setInStorage(true);
        instrumentRepository.save(instrument);
    }
}
