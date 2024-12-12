package uk.ac.sheffield.team28.team28.controller;

//import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uk.ac.sheffield.team28.team28.service.ChildMemberService;
import uk.ac.sheffield.team28.team28.service.MemberService;


@Controller()
@RequestMapping("/child")

public class ChildMemberController {
    private final ChildMemberService childMemberService;


    public ChildMemberController(ChildMemberService childMemberService, MemberService memberService) {
        this.childMemberService = childMemberService;


    }

    @PreAuthorize("@childMemberService.canAccessChildDashboard(#childMemberId, authentication.name)")
    @GetMapping("/dashboard/{childMemberId}")
    public String childDashboard(Model model, @PathVariable Long childMemberId) {
        System.out.println("HERE");
        // Handle the logic for displaying the child's dashboard
        model.addAttribute("childMemberId", childMemberId);
        return "childDashboard";
    }
    

    @GetMapping("allow-to-go-parent")
    public String allowToGoParent(HttpSession session) {
        Boolean isAuthorised = (Boolean) session.getAttribute("isAuthorised");
        session.setAttribute("referer", "/dashboard");
        return "redirect:/authorise";
    }


}
