package uk.ac.sheffield.team28.team28.service;

import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.repository.ChildMemberRepository;

import java.util.List;

public class ChildMemberService {

    private final ChildMemberRepository childMemberRepository;

    public ChildMemberService(ChildMemberRepository childMemberRepository){
        this.childMemberRepository = childMemberRepository;
    }

    public List<ChildMember> getChildByParent(Member parent){
        return childMemberRepository.findAllByParent(parent);
    }
}
