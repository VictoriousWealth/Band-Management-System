package uk.ac.sheffield.team28.team28.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.service.MemberService;

import java.util.List;

@Controller
public class AccountDetailsController {
    private final MemberService memberService;

    public AccountDetailsController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/account-info")
    public String accountInfo(HttpSession session, Model model) {
        Boolean isAuthorized = (Boolean) session.getAttribute("isAuthorized");
        if (isAuthorized == null || !isAuthorized) {
            return "redirect:/authorise";
        }
        Member member = memberService.findMember();
        model.addAttribute("member", member);
        return "account-info";
    }

    @PostMapping("/account/update")
    public String updateAccountInfo(@ModelAttribute Member desiredUpdatedMember, Model model) {
        //TODO all this should happen once the director has allowed the changes
        boolean hasBeenAccepted = false;


        Member oldMember = memberService.findMember(); // old member as in original member with no changes

        if (desiredUpdatedMember != null) {
            List<Exception> exceptionList = memberService.updateMemberInfo(
                    oldMember.getId(), // desired...member won't have an id @see Member model class
                    desiredUpdatedMember.getFirstName(),
                    desiredUpdatedMember.getLastName(),
                    desiredUpdatedMember.getPhone(),
                    desiredUpdatedMember.getEmail()
            );
            if (exceptionList.isEmpty()) {
                Member updatedMember = memberService.findMember(); // yes it is the same as desired...Member
                model.addAttribute("member", updatedMember);
                model.addAttribute("success", true);
            } else {
                Member partiallyUpdatedMember = memberService.findMember(); // because the email might be valid, but the first name might be invalid (i.e. empty)
                model.addAttribute("member", partiallyUpdatedMember); // so it shows under account info as it should, (yes if we were to redirect this line will be un-neded)
                model.addAttribute("success", false);
                model.addAttribute("exceptionList", exceptionList); // all the exceptions occurred during the attempt at updating the record, NOTE: if we were to redirect it won't show on the account-info page, as re-directing causes reset of the model values
            }
            model.addAttribute("hasBeenAccepted", hasBeenAccepted);
            return "account-info";
        }
        model.addAttribute("hasBeenAccepted", hasBeenAccepted);
        return "redirect:/account-info";
    }

}
