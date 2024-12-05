package uk.ac.sheffield.team28.team28.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.sheffield.team28.team28.dto.InstrumentDto;
import uk.ac.sheffield.team28.team28.model.Instrument;
import uk.ac.sheffield.team28.team28.model.Loan;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.repository.InstrumentRepository;
import uk.ac.sheffield.team28.team28.service.InstrumentService;
import uk.ac.sheffield.team28.team28.service.LoanService;
import uk.ac.sheffield.team28.team28.service.MemberService;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private InstrumentRepository instrumentRepository;
    @Autowired
    private InstrumentService instrumentService;


    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        //Get logged in member
        Member member = memberService.findMember();
        model.addAttribute("member", member);
        model.addAttribute("memberType", member.getMemberType().toString());

        //If member is a committee member, get all instruments
        if (member.getMemberType() == MemberType.Committee){

            List<Instrument> instruments = instrumentRepository.findAll();
            model.addAttribute("instruments", instruments);
        }

        List<Loan> memberLoans = loanService.getLoansByMemberId(member.getId());
        model.addAttribute("memberLoans", memberLoans);

        return "dashboard";
    }

    @GetMapping("/loanDetails")
    public String getLoanDetails(@RequestParam Long loanId, Model model) {
        Loan selectedLoan = loanService.findLoanById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        model.addAttribute("selectedLoan", selectedLoan);
        return "dashboard";
    }

}
