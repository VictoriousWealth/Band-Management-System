package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;

@Entity
@Table(name="Item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "itemId", nullable = false)
    private Long itemId;

    @Column(name = "itemType", nullable = false)
    //private Enum itemType;

    @Column(name = "nameTypeOrTitle", nullable = false)
    private String nameTypeOrTitle;

    @Column(name = "makeOrComposer", nullable = true)
    private String makeOrComposer;

    @Column(name = "inStorage", nullable = true)
    private Boolean inStorage;

    @Column(name = "note", nullable = true)
    private String note;

}
