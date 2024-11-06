package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;

@Entity
@Table(name="Item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "itemType", nullable = false)
    private ItemType itemType;

    @Column(name = "nameTypeOrTitle", nullable = false)
    private String nameTypeOrTitle;

    @Column(name = "makeOrComposer", nullable = true)
    private String makeOrComposer;

    @Column(name = "inStorage", nullable = true)
    private Boolean inStorage;

    @Column(name = "note", nullable = true)
    private String note;

    //Getters and setters

    public Item(){}

    public Item(ItemType itemType, String nameTypeOrTitle, String makeOrComposer, String note){

        this.itemType = itemType;
        this.nameTypeOrTitle = nameTypeOrTitle;
        this.makeOrComposer = makeOrComposer;
        inStorage = true;
        this.note = note;
    }

    public Long getId() {
        return id;
    }


    public ItemType getItemType(){
        return itemType;
    }

    public String getNameTypeOrTitle(){
        return nameTypeOrTitle;
    }

    public String getMakeOrComposer(){
        return makeOrComposer;
    }

    public Boolean getInStorage(){
        return inStorage;
    }

    public String getNote(){
        return note;
    }
}
