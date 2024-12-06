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
import uk.ac.sheffield.team28.team28.service.ChildMemberService;

import java.security.Principal;
import java.util.List;
import java.util.Objects;


@Controller()
@RequestMapping("/child")

public class ChildMemberController {
    private final ChildMemberService childMemberService;

    public ChildMemberController(ChildMemberService childMemberService) {
        this.childMemberService = childMemberService;

    }

    @GetMapping("/dashboard/{childMemberId}")
    public String childDashboard(Model model, @PathVariable Long childMemberId) {
        // Handle the logic for displaying the child's dashboard
        model.addAttribute("childMemberId", childMemberId);
        return "childDashboard";
    }


}
