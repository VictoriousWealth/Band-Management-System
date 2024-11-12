package uk.ac.sheffield.team28.team28.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.sheffield.team28.team28.dto.MemberRegistrationDto;
import uk.ac.sheffield.team28.team28.service.MemberService;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

   @PostMapping("/register")
    public ResponseEntity<String> registerMember(@RequestBody MemberRegistrationDto registrationDto) {
        try {
            memberService.registerMember(registrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Member registered successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }

    @GetMapping("test")
    public String testEndpoint() {
        return "Worked";
    }

}
