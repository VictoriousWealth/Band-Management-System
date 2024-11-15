package uk.ac.sheffield.team28.team28.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
import uk.ac.sheffield.team28.team28.dto.MemberLoginDto;
import uk.ac.sheffield.team28.team28.dto.MemberRegistrationDto;
import uk.ac.sheffield.team28.team28.exception.MemberRegistrationException;
import uk.ac.sheffield.team28.team28.service.MemberService;


@Controller
@RequestMapping("/auth")
public class AuthController {
    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("member", new MemberLoginDto());
        return "login";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("member", new MemberRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String performRegister(
            @Valid @ModelAttribute("member") MemberRegistrationDto registrationDto,
            BindingResult result,
            Model model) {
        // Check for validation errors in the DTO
        if (result.hasErrors()) {
            return "register";
        }

        try {
            // Attempt to register the member
            memberService.registerMember(registrationDto);
        } catch (MemberRegistrationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "register";
        }

        model.addAttribute("successMessage", "Registration completed successfully. Please login.");
        return "redirect:/auth/login"; // to avoid form resubmission issues
    }
}
