package uk.ac.sheffield.team28.team28.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import uk.ac.sheffield.team28.team28.model.Member;


@Controller
@RequestMapping("/committee")
public class CommitteeController {
    
    @GetMapping("/dashboard")
    public String committeeDashboard() {
        return "committee-dashboard";
    }
}
