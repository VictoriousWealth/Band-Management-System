package uk.ac.sheffield.team28.team28.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.ac.sheffield.team28.team28.dto.MemberRegistrationDto;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;

import java.security.Principal;
import java.util.Optional;

@Controller()
@RequestMapping("/")
public class HomeController {


    private final MemberRepository memberRepository;

    public HomeController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/")
    public String showHomePage(Model model, Principal principal) {
        if (principal != null && principal.getName() != null) {
            String username = principal.getName();
            Optional<Member> currentUser = memberRepository.findByEmail(username);

            model.addAttribute("currentUser", currentUser);
        }
        return "home";
    }

}
