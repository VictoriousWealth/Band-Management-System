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

    @Column(name = "note")
    private String note;

    public Order() {}

    public Order(Member member, Item item, LocalDate orderDate) {
        this.member = member;
        this.item = item;
        this.orderDate = orderDate;
        this.isFulfilled = false;
        note = null;
    }

    public Order(Member member, Item item, LocalDate orderDate, String note) {
        this.member = member;
        this.item = item;
        this.orderDate = orderDate;
        this.isFulfilled = false;
        this.note = note;
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

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public boolean getIsFulfilled() {
        return isFulfilled;
    }

    public void setIsFulfilled(boolean isFulfilled) {
        this.isFulfilled = isFulfilled;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
