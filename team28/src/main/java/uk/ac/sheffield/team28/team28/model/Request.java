package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "request")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // ManyToOne as each Request is made by one Member
    @JoinColumn(name = "requester_id", nullable = false) // Foreign key column name
    private Member requester;

    @Column(name = "accepted", nullable = false)
    private boolean accepted;

    @Column(name = "description", length = 500)
    private String description;

    // Constructors
    public Request() {}

    public Request(Member requester, boolean accepted, String description) {
        this.requester = requester;
        this.accepted = accepted;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getRequester() {
        return requester;
    }

    public void setRequester(Member requester) {
        this.requester = requester;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return accepted == request.accepted &&
                Objects.equals(id, request.id) &&
                Objects.equals(requester, request.requester) &&
                Objects.equals(description, request.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requester, accepted, description);
    }

    // toString
    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", requester=" + requester +
                ", accepted=" + accepted +
                ", description='" + description + '\'' +
                '}';
    }
}
