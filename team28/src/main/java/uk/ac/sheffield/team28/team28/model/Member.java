package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
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
}

