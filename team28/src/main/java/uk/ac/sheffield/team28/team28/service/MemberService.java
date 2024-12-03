package uk.ac.sheffield.team28.team28.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import uk.ac.sheffield.team28.team28.dto.MemberRegistrationDto;
import uk.ac.sheffield.team28.team28.exception.EmailAlreadyInUseException;
import uk.ac.sheffield.team28.team28.exception.FieldCannotBeBlankException;
import uk.ac.sheffield.team28.team28.exception.IdNotFoundException;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
//import uk.ac.sheffield.team28.team28.exception.MemberRegistrationException;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.repository.ChildMemberRepository;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final ChildMemberRepository childMemberRepository;
     private final PasswordEncoder passwordEncoder;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z]).{8,}$");

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder,
                         ChildMemberRepository childMemberRepository) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.childMemberRepository = childMemberRepository;
    }

     public Member registerMember(MemberRegistrationDto dto) throws Exception {

        // Checking to see whether a member already exists with the same email address
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new Exception("Email is already in use. Please login to continue.");
        }

         // Validating password strength
         if (!isValidPassword(dto.getPassword())) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain at least one uppercase letter.");
        }

         // Check if child member is to be added and validate fields
         boolean addChild = false;
         if (dto.getAddChild()){
             //Validate fields
             if (dto.getChildFirstName().isBlank() || dto.getChildLastName().isBlank()){
                 throw new IllegalStateException("Child first and/or last name cannot be empty");
             } else {
                 addChild = true;
             }
         }

        // Hashing the password
        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        Member member = new Member();
        member.setEmail(dto.getEmail());
        member.setPassword(hashedPassword);
        member.setMemberType(MemberType.Adult);
        member.setPhone(dto.getPhone());
        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
  

        Member savedMember = memberRepository.save(member);

        //Check to see if child is to be added
         if(addChild){
             //Create child entry
             ChildMember child = new ChildMember(
                     dto.getChildFirstName(),
                     dto.getChildLastName(),
                     member);
             childMemberRepository.save(child);
         }


         return savedMember;
    }

    private boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member findMember(){
        String email;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }


        if (memberRepository.findByEmail(email).isPresent()) {
            return memberRepository.findByEmail(email).get();
        } else {
            return null;
        }
    }


    public Member addMemberToCommittee(Long memberId) throws Exception {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new Exception("Member not found with ID: " + memberId));

        if (member.getMemberType() == MemberType.Adult) {
            member.setMemberType(MemberType.Committee);
        } else
            throw new Exception("Member cannot be added.");

        // Save updated member
        memberRepository.save(member);
        return member;
    }

    public Member addMemberToBand(Long memberId, BandInPractice newBand) throws Exception {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new Exception("Member not found with ID: " + memberId));

        if (member.getBand() == newBand) {
            throw new Exception("Member is already in this band.");
        }
        if (member.getBand() == BandInPractice.None) {
            member.setBand(newBand);
        } else if (member.getBand() != BandInPractice.Both) {
            member.setBand(BandInPractice.Both);
        } else {
            throw new Exception("Member is already in both bands.");
        }

        // Save updated member
        memberRepository.save(member);
        return member;
    }


    public Member removeMemberFromBand(Long memberId, BandInPractice oldBand) throws Exception {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new Exception("Member not found with ID: " + memberId));

        if (member.getBand() != BandInPractice.None && member.getBand() == oldBand) {
            member.setBand(BandInPractice.None);
        } else if (member.getBand() == BandInPractice.Both && oldBand == BandInPractice.Training) {
            member.setBand(BandInPractice.Senior);

        } else if (member.getBand() == BandInPractice.Both && oldBand == BandInPractice.Senior) {
            member.setBand(BandInPractice.Training);

        } else
            throw new Exception("Member is already not assigned to any band.");
        //Save changes
        memberRepository.save(member);
        return member;
    }

    public List<Member> getCommitteeMembers() {
        return memberRepository.findByMemberType(MemberType.Committee); //Currently set to ADULT since no committee
    }
    public List<Member> getAdultMembers() {
        return memberRepository.findByMemberType(MemberType.Adult); //Currently set to ADULT since no committee
    }
    public List<Member> getAllMembersBands() {
        List<Member> allMembers = new ArrayList<>();

        allMembers.addAll(memberRepository.findByBand(BandInPractice.None));
        allMembers.addAll(memberRepository.findByBand(BandInPractice.Training));
        allMembers.addAll(memberRepository.findByBand(BandInPractice.Both));
        allMembers.addAll(memberRepository.findByBand(BandInPractice.Senior));

        return allMembers; //Currently set to ADULT since no committee
    }
    public List<Member> getTrainingBandMembers() {
        List<Member> allMembers = new ArrayList<>();

        allMembers.addAll(memberRepository.findByBand(BandInPractice.Training));
        allMembers.addAll(memberRepository.findByBand(BandInPractice.Both));
        return allMembers; //Currently set to ADULT since no committee
    }

    public List<Member> getSeniorBandMembers() {
        List<Member> allMembers = new ArrayList<>();

        allMembers.addAll(memberRepository.findByBand(BandInPractice.Senior));
        allMembers.addAll(memberRepository.findByBand(BandInPractice.Both));
        return allMembers; //Currently set to ADULT since no committee
    }

    public void setNone () {

    }

    public boolean authorise(Long memberId, String password) throws Exception {
        //return true;
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new Exception("Member not found with ID: " + memberId));
        // Compare the hashed password with the provided password
        return passwordEncoder.matches(password,member.getPassword());

    }

    public List<Exception> updateMemberInfo(Long id, String firstName, String lastName, String phone, String email) {
        List<Exception> exceptions = new ArrayList<>();
        System.out.println(id);
        if (memberRepository.findById(id).isPresent()) {
            Member memberToBeUpdated = memberRepository.findById(id).get();

            //TODO check if firstname is in valid format
            if (firstName != null && !firstName.isBlank()) {
                memberToBeUpdated.setFirstName(firstName);
            } else {
                exceptions.add(new FieldCannotBeBlankException("First name cannot be empty"));
            }

            //TODO check if lastname is in valid format
            if (lastName != null && !lastName.isBlank()) {
                memberToBeUpdated.setLastName(lastName);
            } else {
                exceptions.add(new FieldCannotBeBlankException("Last name cannot be empty"));
            }

            //TODO check if phone number is in valid format
            if (phone != null) {
                memberToBeUpdated.setPhone(phone);
            } else {
                exceptions.add(new FieldCannotBeBlankException("Phone cannot be empty"));
            }

            //TODO check if email is in valid format
            if (!email.equals(memberToBeUpdated.getEmail()) && !memberRepository.existsByEmail(email)) {
                memberToBeUpdated.setEmail(email);
            } else if (email.equals(memberToBeUpdated.getEmail())) {
                memberToBeUpdated.setEmail(email);
            } else {
                exceptions.add(new EmailAlreadyInUseException());
            }

            memberRepository.save(memberToBeUpdated);
        } else {
            exceptions.add(new IdNotFoundException());
        }
        return exceptions;
    }

}
