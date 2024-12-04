package uk.ac.sheffield.team28.team28.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.service.ChildMemberService;
import uk.ac.sheffield.team28.team28.service.MemberService;

import java.util.List;

@Controller("/")
public class ChildMemberController {


    @Autowired
    MemberService memberService;
    @Autowired
    ChildMemberService childMemberService;

    @GetMapping("/viewChildren")
    public String viewChildren(Model model) {
        Member currentMember = memberService.findMember();
        model.addAttribute("memberType", currentMember.getMemberType().toString());
        List<ChildMember> children = childMemberService.getChildByParent(currentMember);
        model.addAttribute("children", children);
        return "viewChildren";
    }
}
