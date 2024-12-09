package uk.ac.sheffield.team28.team28.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.sheffield.team28.team28.dto.LoanRequestDto;
import uk.ac.sheffield.team28.team28.service.LoanService;
import uk.ac.sheffield.team28.team28.model.Loan;

import java.io.IOException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/loanAction")
    public ResponseEntity<String> handleLoanAction(@ModelAttribute LoanRequestDto loanRequest, HttpServletResponse response) {
        if (loanRequest.getAction() == null) {
            return ResponseEntity.badRequest().body("Action is required");
        }
        System.out.println(loanRequest.getAction());
        try {
            if (loanRequest.getAction().equals("loan")) {
                loanService.loanInstrument(loanRequest.getInstrumentId(), loanRequest.getMemberName());
            } else if (loanRequest.getAction().equals("return")) {
                loanService.returnInstrument(loanRequest.getInstrumentId());
            } else {
                return ResponseEntity.badRequest().body("Invalid action specified: " + loanRequest.getAction());
            }
            response.sendRedirect("/dashboard");
            return ResponseEntity.ok("Action processed successfully");
        } catch (IllegalStateException | NoSuchElementException | IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
