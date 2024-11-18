package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

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

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "member_type", nullable = false)
    private MemberType memberType;

    @Column(name="phone")
    private String phone;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    // Parent can have multiple children
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Member> children = new HashSet<>();

    // Each child has one parent
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Member parent;

    public Member() {}
    public Member(Long id, String email, String password, String memberType, String phone, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.memberType = MemberType.fromString(memberType);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public Member(Long id, String email, String password, String phone, String firstName, String lastName) {
        this(id, email, password, "Adult", phone, firstName, lastName);
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


public void setEmail(String email) {
    this.email = email;
}

public void setFirstName(String firstName) {
    this.firstName = firstName;
}

public void setLastName(String lastName) {
    this.lastName = lastName;
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

    public Set<Member> getChildren() {
        return children;
    }

    public void setChildren(Set<Member> children) {
        this.children = children;
    }

}

