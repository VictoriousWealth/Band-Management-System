package uk.ac.sheffield.team28.team28.service;

import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.exception.FieldCannotBeBlankException;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.repository.ChildMemberRepository;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class ChildMemberService {

    private final ChildMemberRepository childMemberRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public ChildMemberService(ChildMemberRepository childMemberRepository, MemberService memberService, MemberRepository memberRepository){
        this.childMemberRepository = childMemberRepository;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    public Optional<ChildMember> getChildByFullName(String fullName) {
        //Make sure case is correct
        String[] nameArray = fullName.split(" ");
        StringBuilder capitalizedFullName = new StringBuilder();
        for (String name : nameArray) {
            if (!name.isEmpty()) {
                String capitalized = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                capitalizedFullName.append(capitalized).append(" ");
            }
        }
        String result = capitalizedFullName.toString().trim();
        return childMemberRepository.findByName(result);
    }
    public List<ChildMember> getChildByParent(Member parent){
        return childMemberRepository.findAllByParent(parent);
    }

    public List<ChildMember> getAllChildren(){
        return childMemberRepository.findAllChildren();
    }

    public List<Member> getAllParents(){
        return childMemberRepository.findAllParents();
    }

    public List<ChildMember> getTrainingBandMembers() {
        List<ChildMember> allChildMembers = new ArrayList<>();
        allChildMembers.addAll(childMemberRepository.findByBand(BandInPractice.Training));
        return allChildMembers;
    }
    public
    Map<Member, List<ChildMember>> getParentsWithChildren() {
        List<Member> parents = getAllParents();
        Map<Member, List<ChildMember>> parentsWithChildren = new LinkedHashMap<>();

        for (Member parent : parents) {
            List<ChildMember> children = getChildByParent(parent);
            parentsWithChildren.put(parent, children);
        }
        return parentsWithChildren;
    }

    public ChildMember addChildMemberToBand(Long childMemberId) throws Exception {
        ChildMember childMember = childMemberRepository.findById(childMemberId).orElseThrow(() ->
                new Exception("Child member not found with ID: " + childMemberId));


        if (childMember.getBand() == BandInPractice.Training) {
            throw new Exception("Child member is already in this band.");
        }
        else if (childMember.getBand() == BandInPractice.None) {
            childMember.setBand(BandInPractice.Training);
        }
        // Save updated member
        childMemberRepository.save(childMember);
        return childMember;
    }


    public ChildMember removeChildMemberFromBand(Long childMemberId) throws Exception {
        ChildMember childMember = childMemberRepository.findById(childMemberId).orElseThrow(() ->
                new Exception("Child member not found with ID: " + childMemberId));

        if (childMember.getBand() == BandInPractice.None) {
            throw new Exception("Child member is already not assigned to a band");
        }
        else if (childMember.getBand() == BandInPractice.Training) {
            childMember.setBand(BandInPractice.None);
        }
        //Save changes
        childMemberRepository.save(childMember);
        return childMember;
    }

    public List<Exception> addNewChild(String firstName, String lastName, Long parentId, LocalDate dateOfBirth) {
        List<Exception> exceptions = new ArrayList<>();
        if (firstName == null || firstName.isBlank()) {
            exceptions.add(new FieldCannotBeBlankException("First name cannot be empty!"));
        }
        if (lastName == null || lastName.isBlank()) {
            exceptions.add(new FieldCannotBeBlankException("Last name cannot be empty!"));
        }
        if (dateOfBirth == null) {
            exceptions.add(new FieldCannotBeBlankException("Date of birth cannot be empty!"));
        }

        if (dateOfBirth != null && dateOfBirth.isAfter(LocalDate.now())) {
            exceptions.add(new IllegalArgumentException("Date of birth cannot be in the future!"));
        }

        if (dateOfBirth != null && dateOfBirth.minusDays(1).isAfter(LocalDate.now().minusYears(4))) {
            exceptions.add(new IllegalArgumentException("Child must be 4 years or older!"));
        }
        if (dateOfBirth != null && Period.between(dateOfBirth, LocalDate.now()).getYears() >= 18) {
            exceptions.add(new IllegalArgumentException("Child cannot be 18 years or older!"));
        }
        ChildMember childMember = new ChildMember(firstName, lastName, memberService.getMemberWithId(parentId), dateOfBirth);
        if (exceptions.isEmpty()) {
            childMemberRepository.save(childMember);
        }
        return exceptions;
    }
}
