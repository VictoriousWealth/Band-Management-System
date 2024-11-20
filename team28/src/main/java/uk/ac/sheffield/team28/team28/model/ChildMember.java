package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;

@Entity
@Table(name="ChildMember")
public class ChildMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "parent", nullable = false)
    private Member parent;

    public ChildMember(){}

    public ChildMember(Long id, String firstName, String lastName, Member parent){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.parent = parent;
    }

    public ChildMember(String firstName, String lastName, Member parent){
        this.firstName = firstName;
        this.lastName = lastName;
        this.parent = parent;
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Member getParent() {
        return parent;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setParent(Member parent) {
        this.parent = parent;
    }
}
