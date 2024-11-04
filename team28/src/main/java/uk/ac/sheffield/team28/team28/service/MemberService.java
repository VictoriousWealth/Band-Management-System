package uk.ac.sheffield.team28.team28.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public boolean authenticate(String email, String password) {
        List<Member> membersMatches = // list of the members whose email and password are the same as the one passed
                getAllMembers().stream().filter(member ->
                        member.getEmail().equals(email) && member.getPassword().equals(password)
                                && member.getMemberType()== MemberType.Adult)
                        .toList();
        return !membersMatches.isEmpty(); // if it's empty that means that there was no adult member with such credentials
    }

    public boolean isEmailAlreadyInUse(String email) {
        return getAllMembers().stream().noneMatch(m -> Objects.equals(m.getEmail(), email));
    }

    public boolean isEmailValid(String email) {
        return Pattern.compile("^[A-Za-z0-9_]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$").matcher(email).matches();
    }

    public boolean isPhoneValid(String phone) {
        return Pattern.compile("^[+]+[0-9]{11,}$").matcher(phone).matches();
    }

    public boolean isNameValid(String fullName) {
        return Pattern.compile("^+[A-Za-z-]{2,}$")
                .matcher(fullName.replace(" ", "")).matches();
    }

    public boolean isPasswordValid(String password) {
        return Pattern.compile("^[A-Za-z0-9_@#!%&$£/?|.-]{7,}$")
                .matcher(password).matches();
    }

    public boolean isPasswordStrongEnough(String password) {
        if (password.length() < 7) return false;

        boolean containsNumber = false;
        for (int i = 0; i <= 9; i++) containsNumber |= password.contains(String.valueOf(i));
        if (!containsNumber) return false;

        boolean containsSpecialChar = false;
        for (char c:"_@#!%&$£/?|.-".toCharArray()) containsSpecialChar |= password.contains(String.valueOf(c));
        if (!containsSpecialChar) return false;


        boolean containsUpperCase = false;
        for (char c:"ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray())
            containsUpperCase |= password.contains(String.valueOf(c));
        if (!containsUpperCase) return false;

        boolean containsLowerCase = false;
        for (char c:"abcdefghijklmnopqrstuvwxyz".toCharArray())
            containsLowerCase |= password.contains(String.valueOf(c));
        return containsLowerCase;
    }




}
