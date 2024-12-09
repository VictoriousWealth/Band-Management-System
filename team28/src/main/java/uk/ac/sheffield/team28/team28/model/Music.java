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

    @Enumerated(EnumType.STRING)
    @Column(name = "band_in_practice", nullable = true)
    private BandInPractice bandInPractice;

    @Column(name = "is_suitable_for_training", nullable = false)
    private boolean isSuitableForTraining;

    //Getters and setters
    public Music(){}

    public Music(Item item, BandInPractice bandInPractice, boolean isSuitableForTraining) {
        this.item = item;
        this.bandInPractice = bandInPractice;
        this.isSuitableForTraining = isSuitableForTraining;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public BandInPractice getBandInPractice() {
        return bandInPractice;
    }

    public void setBandInPractice(BandInPractice bandInPractice) {
        this.bandInPractice = bandInPractice;
    }

    public boolean isSuitableForTraining() {
        return isSuitableForTraining;
    }

    public void setSuitableForTraining(boolean suitableForTraining) {
        isSuitableForTraining = suitableForTraining;
    }
}
