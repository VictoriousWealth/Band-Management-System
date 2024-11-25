package uk.ac.sheffield.team28.team28.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.service.MemberService;

@Controller
public class DashboardController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        //Get logged in member
        Member member = memberService.findMember();
        model.addAttribute("memberType", member.getMemberType().toString());
        model.addAttribute("member", member);
        return "dashboard";
    }
}
