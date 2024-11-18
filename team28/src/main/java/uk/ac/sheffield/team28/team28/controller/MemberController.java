package uk.ac.sheffield.team28.team28.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.service.MemberService;


@RestController()
@RequestMapping("/")
public class MemberController {
    private final MemberService memberService;
    private final static Logger logger = LoggerFactory.getLogger(MemberController.class);

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/{memberId}/addToBand")
    public ResponseEntity<Member> addMemberToBand(@PathVariable Long memberId, @PathVariable BandInPractice oldBand) {
        try {
            Member member = memberService.addMemberToBand(memberId, oldBand);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{memberId}/removeFromBand")
    public ResponseEntity<Member> removeMemberFromBand(@PathVariable Long memberId, @PathVariable BandInPractice newBand) {
        try {
            Member member = memberService.removeMemberFromBand(memberId, newBand);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/{memberId}/authorise")
    public ResponseEntity<Member> authorise(@PathVariable Long memberId, @PathVariable String password) {
        try {
            boolean authorised = memberService.authorise(memberId, password);
            return ResponseEntity.ok(null); //May need changing
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
