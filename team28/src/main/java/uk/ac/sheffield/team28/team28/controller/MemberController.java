package uk.ac.sheffield.team28.team28.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.sheffield.team28.team28.service.MemberService;
import uk.ac.sheffield.team28.team28.model.Member;

import java.util.Objects;
import java.util.regex.Pattern;


@RestController()
@RequestMapping("/")
public class MemberController {
    private final MemberService memberService;
    private final static Logger logger = LoggerFactory.getLogger(MemberController.class);

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping(value = "/login", params = {"email", "password"})
    public String login(
            @RequestParam String email,
            @RequestParam String password) {
        String temp;
        if (memberService.authenticate(email, password)) {
            logger.info("login successful");
            temp = "login successful";
        } else {
            logger.info("login failed");
            temp = "login failed";
        }
        return temp+" attempted with "+email+" and "+password;
    }


    @GetMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody Member member) {
        if(!memberService.isEmailValid(member.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email is invalid");
        }

        if (memberService.isEmailAlreadyInUse(member.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email is already in use");
        }

        if (!memberService.isPhoneValid(member.getPhone())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("telephone number is invalid");
        }

        if (!memberService.isPasswordValid(member.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("password contains unacceptable characters " +
                    "or not strong enough. Follow the rules");
        }


        if (memberService.isNameValid(member.getFullName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Name can only contain alphabet characters and dashes");
        }

        //TODO; need to add code that adds it to the member table

        return ResponseEntity.ok("sign-up successful");
    }
}
