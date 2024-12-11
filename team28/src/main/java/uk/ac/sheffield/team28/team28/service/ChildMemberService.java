package uk.ac.sheffield.team28.team28.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.exception.FieldCannotBeBlankException;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.repository.ChildMemberRepository;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;

import uk.ac.sheffield.team28.team28.repository.LoanRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class ChildMemberService {

    private final ChildMemberRepository childMemberRepository;
    private final LoanRepository loanRepository;

    private final MemberService memberService;
    private final MemberRepository memberRepository;




    public ChildMemberService(ChildMemberRepository childMemberRepository, MemberService memberService, MemberRepository memberRepository, LoanRepository loanRepository ){
        this.childMemberRepository = childMemberRepository;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.loanRepository = loanRepository;

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

    public ChildMember getChildById(Long childMemberId) throws Exception {
        ChildMember childMember = childMemberRepository.findById(childMemberId).orElseThrow(() ->
                new Exception("Member not found with ID: " + childMemberId));
        return childMember;
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

    public boolean doAnyChildrenHaveLoans(Member parent) {
        // Get all children for the given parent
        List<ChildMember> allChildren = getChildByParent(parent);
        // Loop through each child and check if they have active loans
        for (ChildMember child : allChildren) {
            if (loanRepository.childHasActiveLoans(child.getId())) {
                // If any child has an active loan, return true
                return true;
            }
        }
        // If no children have active loans, return false
        return false;
    }

    public boolean doesChildHaveLoans(ChildMember child) {
        return loanRepository.childHasActiveLoans(child.getId());
    }

    @Transactional //Delete all a parent's children
    public void deleteChildMembers(Member parent) throws Exception {
        List<ChildMember> allChildren = getChildByParent(parent);
        for (ChildMember child : allChildren) {
            loanRepository.deleteLoansByChildMemberId(child.getId());
            childMemberRepository.deleteById(child.getId());
        }
    }

    @Transactional //Delete a parent's children
    public void deleteChildMember(ChildMember child) throws Exception {
        loanRepository.deleteLoansByChildMemberId(child.getId());
        childMemberRepository.deleteById(child.getId());
    }
    public Map<Member, List<ChildMember>> getParentsWithChildren() {
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

        if (dateOfBirth != null && dateOfBirth.minusDays(1).isBefore(LocalDate.now().minusYears(4))) {
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
