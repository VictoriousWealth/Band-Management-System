package uk.ac.sheffield.team28.team28.service;

import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.repository.ChildMemberRepository;

import java.util.*;

@Service
public class ChildMemberService {

    private final ChildMemberRepository childMemberRepository;

    public ChildMemberService(ChildMemberRepository childMemberRepository){
        this.childMemberRepository = childMemberRepository;
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

    public void save(ChildMember childMember) {
        childMemberRepository.save(childMember);
    }
}
