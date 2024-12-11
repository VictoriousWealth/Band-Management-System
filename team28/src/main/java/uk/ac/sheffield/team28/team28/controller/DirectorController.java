package uk.ac.sheffield.team28.team28.controller;
//todo: Children should not be able to use their (adult proxy) account. Therefore the second part of this question is not relevant.
// TODO:multiple safeguards in place so that this is not accidentally done.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.service.MemberService;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;
import uk.ac.sheffield.team28.team28.service.ChildMemberService;
import uk.ac.sheffield.team28.team28.repository.ChildMemberRepository;

import javax.servlet.http.HttpServletRequest;


import java.util.*;


@Controller // Use @Controller instead of @RestController for Thymeleaf views
@RequestMapping("/director")
public class DirectorController {

    private final MemberService memberService;
    private final MemberRepository memberRepository; // Add this field
    private final ChildMemberService childMemberService;
    private final ChildMemberRepository childMemberRepository; // Add this field



    @Autowired // Constructor injection
    public DirectorController(MemberService memberService, MemberRepository memberRepository, ChildMemberRepository childMemberRepository, ChildMemberService childMemberService) {

        this.memberService = memberService;
        this.memberRepository = memberRepository; // Initialize it in the constructor
        this.childMemberRepository = childMemberRepository;
        this.childMemberService = childMemberService;
    }

    @GetMapping("")
    public String directorHome(Model model) {
        model.addAttribute("message", "This is our director page");
        model.addAttribute("memberType", memberService.findMember().getMemberType().toString());
        return "directorhome";
    }

    @GetMapping("/allMembers")
    public String allMembers(Model model) {
        List<Member> members = memberService.getAllMembers();
        List<ChildMember> children = childMemberService.getAllChildren();
        model.addAttribute("members", members);
        model.addAttribute("children", children);
        return "viewAllMembers";
    }




    @PostMapping("/removeMember/{memberId}")
    public String removeMember(@PathVariable Long memberId, @RequestParam(value = "redirectTo", required = false) String redirectTo,  RedirectAttributes redirectAttributes) {
        try {
            Member member = memberService.findMemberById(memberId);
            boolean hasChildren = !childMemberService.getChildByParent(member).isEmpty();
            if (hasChildren) {
                if (childMemberService.doAnyChildrenHaveLoans(member) == true) {
                    redirectAttributes.addAttribute("error", true);
                }
            }
            //Check parent
            boolean cannotDelete = memberService.doesMemberHaveLoans(member);
            //Delete parent
            if (!cannotDelete) {
                if (hasChildren){
                    childMemberService.deleteChildMembers(member);
                }
                memberService.deleteMember(member.getId());
            }
            else {
                redirectAttributes.addAttribute("error", true);
            }
            redirectAttributes.addAttribute("success", true);

        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
        }
        return "redirect:" + (redirectTo != null ? redirectTo : "/defaultPath");
    }


    @PostMapping("/removeChildMember/{childMemberId}")
    public String removeChildMember(@PathVariable Long childMemberId, @RequestParam(value = "redirectTo", required = false) String redirectTo, RedirectAttributes redirectAttributes) {
        try {
            ChildMember childMember = childMemberService.getChildById(childMemberId);
            boolean cannotDelete = childMemberService.doesChildHaveLoans(childMember);
            //Delete parent
            if (!cannotDelete) {
                childMemberService.deleteChildMember(childMember);
            }
            else {
                redirectAttributes.addAttribute("error", true);
            }
            redirectAttributes.addAttribute("success", true);
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
        }
        return "redirect:" + (redirectTo != null ? redirectTo : "/defaultPath");
    }

    @GetMapping("/committee")
    public String showCommitteeMembers(Model model) {
        List<Member> committeeMembers = memberService.getCommitteeMembers();
        List<Member> nonCommitteeMembers = memberService.getAdultMembers();

        model.addAttribute("committeeMembers", committeeMembers);
        model.addAttribute("nonCommitteeMembers", nonCommitteeMembers);
        model.addAttribute("memberType", memberService.findMember().getMemberType().toString());

        return "dcommittee";
    }


    @GetMapping("/trainingBand")
    public String showTrainingBand(Model model) {
        List<Member> nonBandMembers = memberService.getAllMembersBands();
        List<Member> trainingBandMembers = memberService.getTrainingBandMembers();
        List<ChildMember> nonTrainingBandChildren = childMemberService.getAllChildren();
        List<ChildMember> childTrainingBandMembers = childMemberService.getTrainingBandMembers();
        nonBandMembers.removeAll(trainingBandMembers);
        nonTrainingBandChildren.removeAll(childTrainingBandMembers);
        model.addAttribute("childTrainingBandMembers", childTrainingBandMembers);
        model.addAttribute("nonBandMembers", nonBandMembers);
        model.addAttribute("trainingBandMembers", trainingBandMembers);
        model.addAttribute("nonTrainingBandChildren", nonTrainingBandChildren);
        model.addAttribute("memberType", memberService.findMember().getMemberType().toString());

        return "trainingBand";
    }

    @GetMapping("/seniorBand")
    public String showSeniorBand(Model model) {
        List<Member> nonBandMembers = memberService.getAllMembersBands();
        List<Member> seniorBandMembers = memberService.getSeniorBandMembers();
        nonBandMembers.removeAll(seniorBandMembers);

//        //List<Member> committeeMembers = memberService.getCommitteeMembers();
        model.addAttribute("nonBandMembers", nonBandMembers);
        model.addAttribute("seniorBandMembers", seniorBandMembers);
        model.addAttribute("memberType", memberService.findMember().getMemberType().toString());
        return "seniorBand";
    }


    @PostMapping("/addToBand/{memberId}/{oldBand}")
    public ResponseEntity<String> addMemberToBand(@PathVariable Long memberId, @PathVariable BandInPractice oldBand) {
        try {

            memberService.addMemberToBand(memberId, oldBand);
            return ResponseEntity.ok("Member added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found!");

        }
    }


    @PostMapping("/removeFromBand/{memberId}/{newBand}")
    public ResponseEntity<String> removeMemberFromBand(@PathVariable Long memberId, @PathVariable BandInPractice newBand) {
        try {
            Member member = memberService.removeMemberFromBand(memberId, newBand);
            return ResponseEntity.ok("Member added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found!");
        }
    }

    @PostMapping("/addToBandByEmail")
    public ResponseEntity<String> addMemberToBandByEmail(@RequestParam String email, @RequestParam BandInPractice band) {
        try {
            // Fetch the member by email
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Member not found with email: " + email));
            // Update the member's band
            memberService.addMemberToBand(member.getId(), band);
            // Redirect back to the Training Band page
            return ResponseEntity.ok("Member added successfully!");
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found!");
        }
    }


    @PostMapping("/addChildToBandByName")
    public ResponseEntity<String> addChildToBandByName(@RequestParam String fullName) {
        try {

            String [] names = fullName.split(" ");
            String firstName = names[0];
            String lastName = names[names.length - 1];
            String fullFirstName = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1).toLowerCase();
            String fullLastName = lastName.isEmpty() ? "" : Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1).toLowerCase();
            String newFullName = fullFirstName + " " + fullLastName;
            // Fetch the member by email
            ChildMember childMember = childMemberRepository.findByName(newFullName)
                    .orElseThrow(() -> new RuntimeException("Member not found with email: " + fullName));
            // Update the member's band
            childMemberService.addChildMemberToBand(childMember.getId());
            // Redirect back to the Training Band page
            return ResponseEntity.ok("Member added successfully!");
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found!");
        }
    }

    @PostMapping("/removeChildFromBand/{childMemberId}")
    public ResponseEntity<String> removeMemberFromBand(@PathVariable Long childMemberId) {
        try {
            ChildMember childMember = childMemberService.removeChildMemberFromBand(childMemberId);
            return ResponseEntity.ok("Member added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found!");
        }
    }

    @PostMapping("/addToCommitteeByEmail")
    public ResponseEntity<String> addMemberToCommitteeByEmail(@RequestParam String email) {
        try {
            // Fetch the member by email
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Member not found with email: " + email));

            // Update the member's band
            memberService.promoteMemberWithId(member.getId());

            // Redirect back to the Training Band page
            return ResponseEntity.ok("Member added successfully!");
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found!");
        }
    }


    @GetMapping("/parents")
    public String showParents(Model model) {
        Map<Member, List<ChildMember>> parentsWithChildren = childMemberService.getParentsWithChildren();
        model.addAttribute("parentsWithChildren", parentsWithChildren);
        model.addAttribute("memberType", memberService.findMember().getMemberType().toString());
        System.out.println("IT IS"+parentsWithChildren.isEmpty());
        return "parents";
    }


    @PostMapping("/committee/{id}")
    public ResponseEntity<String> removeMemberFromCommittee(@PathVariable Long id) {
        System.out.println(id);
        try {
            Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
            memberService.demoteMemberWithId(member.getId());
            return ResponseEntity.ok("Committee member demoted");
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return ResponseEntity.badRequest().body("Committee member not found"); // Redirect with an error flag

        }

    }


}
