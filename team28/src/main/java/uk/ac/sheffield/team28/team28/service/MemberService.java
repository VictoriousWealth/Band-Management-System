package uk.ac.sheffield.team28.team28.service;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.dto.MemberLoginDto;
import uk.ac.sheffield.team28.team28.dto.MemberRegistrationDto;
import uk.ac.sheffield.team28.team28.exception.MemberRegistrationException;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;

import java.util.List;
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

}
