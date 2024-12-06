package uk.ac.sheffield.team28.team28.controller;

import org.springframework.beans.factory.annotation.Autowired;
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


import java.util.List;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;


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

    public String redirectToPreviousPage(HttpServletRequest request) {
        // Get the referer URL from the request
        String referer = request.getHeader("Referer");

        // If the referer is null or empty, you can fall back to a default page
        if (referer == null || referer.isEmpty()) {
            referer = "/defaultPage"; // Provide a default URL in case the referer is missing
        }

        // Redirect to the previous page
        return "redirect:" + referer;
    }

    @GetMapping("")
    public String directorHome(Model model) {
        model.addAttribute("message", "This is our director page");
        model.addAttribute("memberType", memberService.findMember().getMemberType().toString());
        return "directorhome";
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

    //Lab7 ajax
    @PostMapping("/addToBand/{memberId}/{oldBand}")
    public String addMemberToBand(@PathVariable Long memberId, @PathVariable BandInPractice oldBand) {
        try {
            memberService.addMemberToBand(memberId, oldBand);
            return "redirect:/director"; // Redirect to PREVIOUS PAGE
        } catch (Exception e) {
            return "redirect:/director"; // REDIRECT TO MEANINFUL PAGE

        }
    }


    @PostMapping("/removeFromBand/{memberId}/{newBand}")
    public String removeMemberFromBand(@PathVariable Long memberId, @PathVariable BandInPractice newBand) {
        try {
            Member member = memberService.removeMemberFromBand(memberId, newBand);
            return "redirect:/director";
        } catch (Exception e) {
            return "redirect:/director"; // Redirect to MEANINGFUL PAGE
        }
    }
//Delete ------------------------------------------------------------------------------------------------------------------
    @GetMapping("/showMemberT/{memberId}")
    public String showMemberT(@PathVariable Long memberId, Model model) {
        //List<Member> committeeMembers = memberService.getCommitteeMembers();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));

        model.addAttribute("member", member);
        model.addAttribute("memberType", memberService.findMember().getMemberType().toString());
        System.out.println("Here" + memberId);
        return "memberDV";

    }
//Delete
    @GetMapping("/showMemberS/{memberId}")
    public String showMemberS(@PathVariable Long memberId, Model model) {
        //List<Member> committeeMembers = memberService.getCommitteeMembers();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));

        model.addAttribute("member", member);
        model.addAttribute("memberType", memberService.findMember().getMemberType().toString());
        System.out.println("Here" + memberId);
        return "memberSB";

    }
//Delete ------------------------------------------------------------------------------------------------------------------

    @PostMapping("/addToBandByEmail")
    public String addMemberToBandByEmail(@RequestParam String email, @RequestParam BandInPractice band) {
        try {
            // Fetch the member by email
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Member not found with email: " + email));

            // Update the member's band
            memberService.addMemberToBand(member.getId(), band);


            // Redirect back to the Training Band page
            return "redirect:/director";
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return "redirect:/director?error=true"; // Redirect with an error flag
        }
    }



    @PostMapping("/addChildToBandByName")
    public String addChildToBandByName(@RequestParam String fullName) {
        try {
            // Fetch the member by email
            ChildMember childMember = childMemberRepository.findByName(fullName)
                    .orElseThrow(() -> new RuntimeException("Member not found with email: " + fullName));
            // Update the member's band
            childMemberService.addChildMemberToBand(childMember.getId());
            // Redirect back to the Training Band page
            return "redirect:/director/trainingBand";
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return "redirect:/director/trainingBand?error=true"; // Redirect with an error flag
        }
    }

    @PostMapping("/removeChildFromBand/{childMemberId}")
    public String removeMemberFromBand(@PathVariable Long childMemberId) {
        try {
            ChildMember childMember = childMemberService.removeChildMemberFromBand(childMemberId);
            return "redirect:/director/trainingBand";
        } catch (Exception e) {
            return "redirect:/director/trainingBand?error=true"; // Redirect with an error flag
        }
    }

    @PostMapping("/addToCommitteeByEmail")
    public String addMemberToCommitteeByEmail(@RequestParam String email) {
        try {
            // Fetch the member by email
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Member not found with email: " + email));

            // Update the member's band
            memberService.promoteMemberWithId(member.getId());

            // Redirect back to the Training Band page
            return "redirect:/director/committee";
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return "redirect:/director/committee?error=true"; // Redirect with an error flag
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
