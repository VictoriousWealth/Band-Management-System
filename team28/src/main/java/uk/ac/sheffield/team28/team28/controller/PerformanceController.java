package uk.ac.sheffield.team28.team28.controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerformanceController {

    @GetMapping("/performance")
    public String calender(Model model) {
        Map<LocalDate, List<String>> performance = new HashMap<>();
        model.addAttribute("performance", performance);
        return "performance";
    }
}
