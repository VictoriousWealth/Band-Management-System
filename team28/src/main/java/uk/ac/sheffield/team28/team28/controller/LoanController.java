package uk.ac.sheffield.team28.team28.controller;

import uk.ac.sheffield.team28.team28.service.LoanService;
import uk.ac.sheffield.team28.team28.model.Loan;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody Loan loan) {
        Loan createdLoan = loanService.save(loan);
        return ResponseEntity.ok(createdLoan);
    }

    // More methods need to be added here later but I'm not sure what exactly is needed yet
}
