package uk.ac.sheffield.team28.team28.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.ac.sheffield.team28.team28.repository.PerformanceRepository;
import uk.ac.sheffield.team28.team28.service.MemberService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PerformanceController {
    private final MemberService memberService;

    public PerformanceController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/performance")
    public String calender(Model model) {

        return "performance";
    }
}
