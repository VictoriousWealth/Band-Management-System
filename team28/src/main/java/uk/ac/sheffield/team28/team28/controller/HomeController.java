package uk.ac.sheffield.team28.team28.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.sheffield.team28.team28.dto.MemberRegistrationDto;

@Controller()
@RequestMapping("/")
public class HomeController {


    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("member", new MemberRegistrationDto());
        return "home";
    }

}
