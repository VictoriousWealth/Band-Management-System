package uk.ac.sheffield.team28.team28.service;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.dto.MemberLoginDto;
import uk.ac.sheffield.team28.team28.dto.MemberRegistrationDto;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.exception.MemberRegistrationException;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$");

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member registerMember(MemberRegistrationDto dto) throws Exception {

        // Check if a member with the given email already exists
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new MemberRegistrationException("Email is already in use. Please login to continue.");
        }

        // Validate password strength
        if (!isValidPassword(dto.getPassword())) {
            throw new MemberRegistrationException("Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and one digit.");
        }

        // Validate password confirmation
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new MemberRegistrationException("Passwords do not match. Please confirm your password.");
        }

        // Hashing the password
        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        // Create a new member and populate fields
        Member member = new Member();
        member.setEmail(dto.getEmail());
        member.setPassword(hashedPassword);
        member.setMemberType(MemberType.Adult); // all members are by default adult members
        member.setPhone(dto.getPhone());
        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());

        // Save the member to the database
        return memberRepository.save(member);
    }

    private boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member authenticateMember(MemberLoginDto loginDto) throws Exception {
        Member member = memberRepository.findByEmail(loginDto.getEmail());

        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            System.out.println(member.getPassword() +","+ loginDto.getPassword());
            throw new Exception("Invalid email or password");
        }

        return member;
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
        return memberRepository.findByMemberType(MemberType.Adult); //Currently set to ADULT since no committee
    }

    public boolean authorise(Long memberId, String password) throws Exception {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new Exception("Member not found with ID: " + memberId));
        //Compares the passwords and returns the boolean result
        return Objects.equals(member.getPassword(), password);


    }


}

