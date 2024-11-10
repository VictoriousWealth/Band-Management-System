package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Music")
public class Music {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "Arranger")
    private String arranger;

    @Column(name = "BandInPractice")
    private BandInPractice bandInPractice;

    //Getters and setters
    public Music(){}

    public Music(Item item, String arranger, BandInPractice bandInPractice){
        this.item = item;
        this.arranger = arranger;
        this.bandInPractice =bandInPractice;
    }

    public Long getId() {
        return id;
    }

    public String getArranger(){
        return arranger;
    }

    public BandInPractice getBandInPractice(){
        return bandInPractice;
    }
}
