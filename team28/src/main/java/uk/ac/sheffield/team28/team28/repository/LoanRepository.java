package uk.ac.sheffield.team28.team28.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.sheffield.team28.team28.model.Loan;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByMemberId(Long memberId);
    List<Loan> findByItemId(Long itemId);
}