package uk.ac.sheffield.team28.team28.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.sheffield.team28.team28.dto.LoanRequestDto;
import uk.ac.sheffield.team28.team28.service.LoanService;
import uk.ac.sheffield.team28.team28.model.Loan;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/create")
    public ResponseEntity<Loan> createLoan(@RequestBody Loan loan) {
        Loan createdLoan = loanService.save(loan);
        return ResponseEntity.ok(createdLoan);
    }

    @PostMapping("/loanAction")
    public ResponseEntity<?> handleLoanAction(@RequestBody LoanRequestDto loanRequest) {
        if (loanRequest.getAction().equals("loan")) {
            loanService.loanInstrument(loanRequest.getInstrumentId(), loanRequest.getMemberName());
        } else if (loanRequest.getAction().equals("return")) {
            loanService.returnInstrument(loanRequest.getInstrumentId());
        }
        return ResponseEntity.ok().build();
    }

}
