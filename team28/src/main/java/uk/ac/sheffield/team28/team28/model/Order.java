package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "orderDate", nullable = false)
    private LocalDate orderDate;

    @Column(name = "isFulfilled")
    private boolean isFulfilled;

    public Order() {}

    public Order(Member member, Item item, LocalDate orderDate) {
        this.member = member;
        this.item = item;
        this.orderDate = orderDate;
        this.isFulfilled = false;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Item getItem() {
        return item;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate loanDate) {
        this.orderDate = orderDate;
    }

    public boolean getIsFulfilled() {
        return isFulfilled;
    }

    public void setIsFulfilled(boolean isFulfilled) {
        this.isFulfilled = isFulfilled;
    }
}
