package uk.ac.sheffield.team28.team28.controller;

//import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.service.ChildMemberService;
import uk.ac.sheffield.team28.team28.service.MemberService;
import uk.ac.sheffield.team28.team28.model.Member;

import java.security.Principal;
import java.util.List;
import java.util.Objects;


@Controller()
@RequestMapping("/")

public class MemberController {
    private final MemberService memberService;
    private final ChildMemberService childMemberService;
    private final static Logger logger = LoggerFactory.getLogger(MemberController.class);

    public MemberController(MemberService memberService, ChildMemberService childMemberService) {
        this.memberService = memberService;
        this.childMemberService = childMemberService;

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
        model.addAttribute("memberType", member.getMemberType().toString());
        model.addAttribute("We have got the page");
        return "authorise"; // This loads the HTML page
    }

    //Add a success message
    @PostMapping("/authorise")
    public String authorise(
            @RequestParam String password,
            Model model, HttpSession session) {
        Member member = memberService.findMember();
        model.addAttribute("memberType", member.getMemberType().toString());
        System.out.println(model.getAttribute("memberType"));
        System.out.println("Received memberId: " + member.getId()); // Log the value
        System.out.println("Received password: " + password); // Log the password
        System.out.println("Received password: " + member.getPassword()); // Log the password

        String referer = session.getAttribute("referer").toString();
        try {
            boolean authorised = memberService.authorise(member.getId(), password);
            if (authorised) {
                session.setAttribute("isAuthorised", true); // Store auth status in session
                String safeReferer = (referer != null) ? referer : "/dashboard";
                model.addAttribute("message", true);
                return "redirect:" + safeReferer;
            } else {
                model.addAttribute("error", "Invalid password. Please try again.");
                return "authorise"; // Reload the page with the error
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "authorise"; // Reload the page with the error
        }
    }

    @PostMapping("/addChild")
    public String addChild(@RequestParam String firstName,
                           @RequestParam String lastName,
                           Model model) {
        try {
            Member parent = memberService.findMember();

            System.out.println("The member's id is: "+ parent.getFirstName());
            System.out.println("Parent: " + parent);
            System.out.println("Parent ID: " + parent.getId());
            System.out.println("Parent First Name: " + parent.getFirstName());
            // Create and save the new child
            ChildMember child = new ChildMember();
            String formattedFirstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
            String formattedLastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);

            child.setFirstName(formattedFirstName);
            child.setLastName(formattedLastName);
            child.setParent(parent);
            childMemberService.save(child);
            return "redirect:/dashboard";

        } catch (Exception e) {
            Member parent = memberService.findMember();

            e.printStackTrace();
                model.addAttribute("error", "An error occurred: " + e.getMessage());
                return "error"; // Return an error page if something goes wrong
            }

    }


}
