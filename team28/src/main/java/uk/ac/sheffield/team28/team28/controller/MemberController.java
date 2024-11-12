package uk.ac.sheffield.team28.team28.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import uk.ac.sheffield.team28.team28.service.MemberService;


@RestController()
@RequestMapping("/")
public class MemberController {
    private final MemberService memberService;
    private final static Logger logger = LoggerFactory.getLogger(MemberController.class);

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }



}
