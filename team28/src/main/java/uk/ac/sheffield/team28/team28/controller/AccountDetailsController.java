package uk.ac.sheffield.team28.team28.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.dto.MemberRegistrationDto;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.Request;
import uk.ac.sheffield.team28.team28.service.ChildMemberService;
import uk.ac.sheffield.team28.team28.service.MemberService;
import uk.ac.sheffield.team28.team28.service.RequestService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AccountDetailsController {
    private final MemberService memberService;
    private final ChildMemberService childMemberService;

    private final RequestService requestService;

    public AccountDetailsController(MemberService memberService, ChildMemberService childMemberService, RequestService requestService) {
        this.memberService = memberService;
        this.requestService = requestService;
        this.childMemberService = childMemberService;
    }

    @GetMapping("/account-info")
    public String accountInfo(HttpSession session, Model model) {
        Member member = memberService.findMember();
        List<ChildMember> children = childMemberService.getChildByParent(member);
        model.addAttribute("member", member);
        model.addAttribute("children", children);

        model.addAttribute("memberType", member.getMemberType().toString());
        model.addAttribute("isAuthorised", session.getAttribute("isAuthorised"));
        System.out.println(member.getMemberType());
        return "account-info";
    }

    @PostMapping("/account/request-update")
    public String requestToUpdateAccountInfo(@ModelAttribute Member desiredUpdatedMember,
                                             HttpSession session, Model model) {
        Boolean isAuthorised = (Boolean) session.getAttribute("isAuthorised");
        Member oldMember = memberService.findMember(); // old member as in original member with no changes
        model.addAttribute("memberType", oldMember.getMemberType().toString());
        if (isAuthorised == null || !isAuthorised) {
            session.setAttribute("referer", "/account-info");
            return "redirect:/authorise";
        }

        // checking if desiredMember is valid
        List<String> errors = new ArrayList<>();
        if (desiredUpdatedMember.getFirstName() == null || desiredUpdatedMember.getFirstName().isBlank()) {
            desiredUpdatedMember.setFirstName(oldMember.getFirstName());
            errors.add("First name cannot be blank.");
        }
        if (desiredUpdatedMember.getLastName()==null || desiredUpdatedMember.getLastName().isBlank()) {
            desiredUpdatedMember.setLastName(oldMember.getLastName());
            errors.add("Last name cannot be blank.");
        }
        if (desiredUpdatedMember.getEmail()==null || desiredUpdatedMember.getEmail().isBlank()) {
            desiredUpdatedMember.setEmail(oldMember.getEmail());
            errors.add("Email cannot be blank.");
        }
        if (desiredUpdatedMember.getPhone()==null || desiredUpdatedMember.getPhone().isBlank()) {
            desiredUpdatedMember.setPhone(oldMember.getPhone());
            errors.add("Phone cannot be blank.");
        }
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            session.removeAttribute("isAuthorised");
            return "account-info";
        }

        String description = getString(desiredUpdatedMember, oldMember);
        requestService.addRequest(new Request(oldMember, false, description));

        session.removeAttribute("isAuthorised");
        model.addAttribute("hasBeenRequested", true);
        return "account-info";
    }


    private static String getString(Member desiredUpdatedMember, Member oldMember) {
        String oldMemberInStringFormat = "{First Name: " + oldMember.getFirstName() + ", Last Name: " + oldMember.getLastName() + ", Email: " + oldMember.getEmail() + ", Phone: " + oldMember.getPhone()+"}";
        String desiredUpdatedMemberInStringFormat = "{First Name: " + desiredUpdatedMember.getFirstName() + ", Last Name: " + desiredUpdatedMember.getLastName() + ", Email: " + desiredUpdatedMember.getEmail() + ", Phone: " + desiredUpdatedMember.getPhone()+"}";
        return "User wants to change account info from: "+oldMemberInStringFormat+" to "+desiredUpdatedMemberInStringFormat;
    }

    @PostMapping("/account/update/{id}")
    public String actualUpdateAccountInfo(@PathVariable Long id, Model model) {
        List<String> details = getMemberUpdatedDetails(requestService.getRequestWithId(id).getDescription());

        Member oldMember = memberService.findMember(); // old member as in original member with no changes

        if (!details.contains("")) {
            List<Exception> exceptionList = memberService.updateMemberInfo(
                    oldMember.getId(),
                    details.get(0), // first name
                    details.get(1), // last name
                    details.get(2), // phone
                    details.get(3) // email
            );
            if (exceptionList.isEmpty()) {
                Member updatedMember = memberService.findMember();
                model.addAttribute("member", updatedMember);
                model.addAttribute("success", true);
            } else {
                Member partiallyUpdatedMember = memberService.findMember(); // because the email might be valid, but the first name might be invalid (i.e. empty)
                model.addAttribute("member", partiallyUpdatedMember); // so it shows under account info as it should, (yes if we were to redirect this line will be un-neded)
                model.addAttribute("success", false);
                model.addAttribute("exceptionList", exceptionList); // all the exceptions occurred during the attempt at updating the record, NOTE: if we were to redirect it won't show on the account-info page, as re-directing causes reset of the model values
            }
            model.addAttribute("hasBeenAccepted", false);
            model.addAttribute("memberType", memberService.findMember().getMemberType().toString());
            requestService.deleteRequest(requestService.getRequestWithId(id));
            model.addAttribute("requests", requestService.getAllApprovedRequestWhereRequesterIs(memberService.findMember()));
            return "account-info";
        }
        model.addAttribute("hasBeenAccepted", false);
        model.addAttribute("memberType", memberService.findMember().getMemberType().toString());
        return "redirect:/account-info";

    }

    private List<String> getMemberUpdatedDetails(String description) {
        String newInfo = description.substring(description.indexOf("} to {"));
        String actualNewInfo = newInfo.substring(newInfo.indexOf("{"));
        String firstName = actualNewInfo.substring(actualNewInfo.indexOf("First Name: ")+"First Name: ".length(), actualNewInfo.indexOf(", Last Name: "));
        String lastName = actualNewInfo.substring(actualNewInfo.indexOf("Last Name: ")+"Last Name: ".length(), actualNewInfo.indexOf(", Email: "));
        String email = actualNewInfo.substring(actualNewInfo.indexOf(" Email: ")+" Email:".length(), actualNewInfo.indexOf(", Phone: "));
        String phone = actualNewInfo.substring(actualNewInfo.indexOf(" Phone: ")+" Phone: ".length(), actualNewInfo.indexOf("}"));

        List<String> details = new ArrayList<>();
        details.add(firstName.strip());
        details.add(lastName.strip());
        details.add(phone.strip());
        details.add(email.strip());

        System.out.println(details);

        return details;
    }

}
