package uk.ac.sheffield.team28.team28.service;

import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.model.Loan;
import uk.ac.sheffield.team28.team28.repository.LoanRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {


    private final LoanRepository loanRepository;

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
}
