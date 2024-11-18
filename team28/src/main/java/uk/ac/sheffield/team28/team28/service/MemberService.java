package uk.ac.sheffield.team28.team28.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import uk.ac.sheffield.team28.team28.dto.MemberRegistrationDto;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
@Service
public class MemberService {
    private final MemberRepository memberRepository;
     private final PasswordEncoder passwordEncoder;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z]).{8,}$");

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
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

        // Hashing the password
        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        Member member = new Member();
        member.setEmail(dto.getEmail());
        member.setPassword(hashedPassword);
        // member.setMemberType(dto.getMemberType() != null ? dto.getMemberType() : MemberType.ADULT);
        member.setPhone(dto.getPhone());
        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
  

        return memberRepository.save(member);
    }

    private boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }


    public Member addMemberToBand(Long memberId, BandInPractice newBand) throws Exception {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new Exception("Member not found with ID: " + memberId));

        if (member.getBand() != newBand && member.getBand() != BandInPractice.None && member.getBand() != BandInPractice.Both) {
            member.setBand(BandInPractice.Both);
        } else if (member.getBand() == BandInPractice.None){
            member.setBand((newBand));
        } else {
            throw new Exception("Member is already in this band.");
        }
        //Save it
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
        return memberRepository.findByMemberType(MemberType.Committee);
    }

    public boolean authorise(Long memberId, String password) throws Exception {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new Exception("Member not found with ID: " + memberId));
        //Compares the passwords and returns the boolean result
        return Objects.equals(member.getPassword(), password);



    }

}
