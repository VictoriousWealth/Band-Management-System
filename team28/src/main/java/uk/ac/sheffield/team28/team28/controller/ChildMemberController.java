package uk.ac.sheffield.team28.team28.controller;

//import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uk.ac.sheffield.team28.team28.model.*;
import uk.ac.sheffield.team28.team28.repository.MusicRepository;
import uk.ac.sheffield.team28.team28.service.ChildMemberService;
import uk.ac.sheffield.team28.team28.service.LoanService;

import java.security.Principal;
import java.util.List;
import java.util.Objects;


@Controller()
@RequestMapping("/child")

public class ChildMemberController {
    private final ChildMemberService childMemberService;

    private final LoanService loanService;

    private final MusicRepository musicRepository;

    public ChildMemberController(ChildMemberService childMemberService, MusicRepository musicRepository, LoanService loanService) {
        this.childMemberService = childMemberService;
        this.musicRepository = musicRepository;
        this.loanService = loanService;

    }

    @GetMapping("/dashboard/{childMemberId}")
    public String childDashboard(Model model, @PathVariable Long childMemberId) throws Exception{
        // Handle the logic for displaying the child's dashboard
        model.addAttribute("childMemberId", childMemberId);

        //Get child member object
        ChildMember member = childMemberService.getChildById(childMemberId);


        //Get performance schedule

        //Get loans
        List<Loan> memberLoans = loanService.getActiveLoansByChildMemberId(member.getId());
        model.addAttribute("memberLoans", memberLoans);

        //Get Music
        BandInPractice band = member.getBand();
        if (band != BandInPractice.None) {
            List<Music> music =
                    musicRepository.findByBandInPracticeOrBandInPractice(band, BandInPractice.Both);
            model.addAttribute("musics", music);
        }

        return "childDashboard";
    }

    @GetMapping("allow-to-go-parent")
    public String allowToGoParent(HttpSession session) {
        Boolean isAuthorised = (Boolean) session.getAttribute("isAuthorised");
        session.setAttribute("referer", "/dashboard");
        return "redirect:/authorise";
    }


}
