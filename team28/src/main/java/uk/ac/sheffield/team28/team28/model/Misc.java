package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Misc")
public class Misc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "Quantity")
    private int quantity;

    @Column(name = "SubClass")
    private String subClass;

    @Column(name = "AmountOnLoan")
    private int amountOnLoan;

    //Getters and setters
    public Misc(){}

    public Misc(Item item, int quantity, String subClass, int amountOnLoan){
        this.item = item;
        this.quantity = quantity;
        this.subClass = subClass;
        this.amountOnLoan = amountOnLoan;
    }

    public Long getId() {
        return id;
    }

    public Item getItem(){
        return item;
    }

    public int getQuantity(){
        return quantity;
    }

    public String getSubClass(){
        return subClass;
    }

    public int getAmountOnLoan(){
        return amountOnLoan;
    }
}
