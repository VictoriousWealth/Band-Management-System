package uk.ac.sheffield.team28.team28.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.service.MemberService;

@Controller
public class AccountDetailsController {
    private final MemberService memberService;

    public AccountDetailsController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/account-info")
    public String accountInfo(Model model) {

        Member member = memberService.findMember();
        model.addAttribute("member", member);
        return "account-info";
    }
}
