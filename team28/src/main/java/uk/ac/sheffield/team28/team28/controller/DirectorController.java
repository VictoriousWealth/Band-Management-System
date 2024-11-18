package uk.ac.sheffield.team28.team28.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.service.MemberService;

import java.util.List;

@Controller // Use @Controller instead of @RestController for Thymeleaf views
@RequestMapping("/director")
public class DirectorController {

    private final MemberService memberService;

    @Autowired // Constructor injection
    public DirectorController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("")
    public String directorHome(Model model) {
        model.addAttribute("message", "This is our director page");
        return "directorhome";
    }

    @GetMapping("/committee")
    public String showCommitteeMembers(Model model) {
        List<Member> committeeMembers = memberService.getCommitteeMembers();
        model.addAttribute("committeeMembers", committeeMembers);
        return "dcommittee";
    }
}
