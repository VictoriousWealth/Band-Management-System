package uk.ac.sheffield.team28.team28.service;

import uk.ac.sheffield.team28.team28.repository.ChildMemberRepository;

public class ChildMemberService {

    private final ChildMemberRepository childMemberRepository;

    public ChildMemberService(ChildMemberRepository childMemberRepository){
        this.childMemberRepository = childMemberRepository;
    }
}
