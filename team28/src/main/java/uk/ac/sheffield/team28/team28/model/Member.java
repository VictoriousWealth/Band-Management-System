package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "Please enter a valid email address")
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Please enter a password")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Column(name = "memberType")
    private MemberType memberType;

    @Column(name="phone")
    private String phone;

    @Column(name="fullName")
    private String fullName;

    public Member() {}
    public Member(Long id, String email, String password, String memberType, String phone, String fullName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.memberType = MemberType.fromString(memberType);
        this.fullName = fullName;
        this.phone = phone;
    }

    public Member(Long id, String email, String password, String phone, String fullName) {
        this(id, email, password, "Adult", phone, fullName);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public String getPhone() {
        return phone;
    }

    public String getFullName() {
        return fullName;
    }


public void setEmail(String email) {
    this.email = email;
}

public void setPassword(String password) {
    this.password = password;
}

public void setMemberType(MemberType memberType) {
    this.memberType = memberType;
}

public void setPhone(String phone) {
    this.phone = phone;
}

public void setFullName(String fullName) {
    this.fullName = fullName;
}
}

