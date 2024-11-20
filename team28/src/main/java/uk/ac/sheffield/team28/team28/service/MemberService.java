package uk.ac.sheffield.team28.team28.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import uk.ac.sheffield.team28.team28.dto.MemberRegistrationDto;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.repository.ChildMemberRepository;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;

import java.util.List;
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
         boolean addChild = true;
         if (dto.getAddChild()){
             //Validate fields
             if (dto.getChildFirstName().isBlank() || dto.getChildLastName().isBlank()){
                 addChild = false;
                 throw new IllegalStateException("Child first and/or last name cannot be empty");
             }
         }

        // Hashing the password
        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        Member member = new Member();
        member.setEmail(dto.getEmail());
        member.setPassword(hashedPassword);
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




}