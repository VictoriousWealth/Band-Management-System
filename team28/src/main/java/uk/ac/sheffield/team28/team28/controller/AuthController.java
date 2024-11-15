package uk.ac.sheffield.team28.team28.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import uk.ac.sheffield.team28.team28.model.Member;



@Controller
@RequestMapping("/auth")
public class AuthController {
    private final MemberService memberService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("member", new MemberLoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String performLogin(
            @Valid @ModelAttribute("member") MemberLoginDto loginDto,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return "login";
        }
        try {
            Member member = memberService.authenticateMember(loginDto);
            model.addAttribute("loggedInUser", member);
            return "redirect:/dashboard";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "login";
        }
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
        logger.info("Attempting to register a new member: {}", registrationDto.getEmail());

        if (!registrationDto.isPasswordMatching()) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "Passwords do not match");
        }

        // Check for validation errors in the DTO
        if (result.hasErrors()) {
            logger.error("Validation errors: {}", result.getAllErrors());
            return "register";
        }

        try {
            memberService.registerMember(registrationDto);
        } catch (MemberRegistrationException e) {
            logger.error("Registration failed: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        } catch (Exception e) {
            logger.error("Unexpected error during registration", e);
            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "register";
        }

        logger.info("Registration successful for: {}", registrationDto.getEmail());
        model.addAttribute("successMessage", "Registration completed successfully. Please login.");
        return "redirect:/auth/login";
    }
}
