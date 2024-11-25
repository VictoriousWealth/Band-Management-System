package uk.ac.sheffield.team28.team28.controller;

//import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.service.MemberService;
import uk.ac.sheffield.team28.team28.model.Member;


@Controller()
@RequestMapping("/")

public class MemberController {
    private final MemberService memberService;
    private final static Logger logger = LoggerFactory.getLogger(MemberController.class);

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @PostMapping("/{memberId}/addToBand")
    public ResponseEntity<Member> addMemberToBand(@PathVariable Long memberId, @PathVariable BandInPractice oldBand) {
        try {
            Member member = memberService.addMemberToBand(memberId, oldBand);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{memberId}/removeFromBand")
    public ResponseEntity<Member> removeMemberFromBand(@PathVariable Long memberId, @PathVariable BandInPractice newBand) {
        try {
            Member member = memberService.removeMemberFromBand(memberId, newBand);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/authorise")
    public String showAuthorisePage(Model model) {
        Member member = memberService.findMember();
        System.out.println("Received memberId: " + member.getFirstName()); // Log the value
        model.addAttribute("member", member);
        model.addAttribute("We have got the page");
        return "authorise"; // This loads the HTML page
    }

    //Add a success message
    @PostMapping("/authorise")
    public String authorise(
            @RequestParam String password,
           // @RequestHeader(value = "Referer", required = false) String referer, //Bring this back when actually implemented
            Model model) {
        Member member = memberService.findMember();
        System.out.println("Received memberId: " + member.getId()); // Log the value
        System.out.println("Received password: " + password); // Log the password
        System.out.println("Received password: " + member.getPassword()); // Log the password

        try {
            boolean authorised = memberService.authorise(member.getId(), password);
            if (authorised) {
                model.addAttribute("message", "Authorisation successful!");
                // Redirect to the referer or home if the referer is null
                //return "redirect:" + (referer != null ? referer : "/home");
                return "home"; // Success page
            } else {
                model.addAttribute("error", "Invalid password. Please try again.");
                return "authorise"; // Reload the page with the error
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "authorise"; // Reload the page with the error
        }
    }

    @GetMapping("/account-info")
    public String accountInfo(Model model) {
//        model.addAttribute("member", );
        return "account-info";

    }


}
