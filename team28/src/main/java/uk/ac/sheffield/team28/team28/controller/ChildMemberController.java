package uk.ac.sheffield.team28.team28.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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


    @PostMapping("/addChild")
    public String addChild(@ModelAttribute("childMember") ChildMember childMember, Model model) {
        Member parentMember = memberService.findMember();

        List<Exception> exceptions = childMemberService.addNewChild(
                childMember.getFirstName(),
                childMember.getLastName(),
                parentMember.getId(),
                childMember.getDateOfBirth()
        );
        if (exceptions.isEmpty()) {
            return "redirect:/viewChildren";
        } else {
            model.addAttribute("exceptions", exceptions);
            StringBuilder reportOfErrors = new StringBuilder("Invalid details submitted. Please try again!");
            for (Exception exception : exceptions) {
                reportOfErrors.append("\n").append(exception.getMessage());
            }
            model.addAttribute("errorMessage", reportOfErrors.toString());
            model.addAttribute("memberType", parentMember.getMemberType().toString());
            model.addAttribute("children", childMemberService.getChildByParent(parentMember));
            return "viewChildren";
        }


    }
}
