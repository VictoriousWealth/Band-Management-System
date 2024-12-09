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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "composer", nullable = false)
    private String composer;

    @Column(name = "arranger")
    private String arranger;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MusicStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "band_in_practice")
    private BandInPractice bandInPractice;

    @Column(name = "is_suitable_for_training", nullable = false)
    private boolean isSuitableForTraining;

    //Getters and setters
    public Music(){}

    public Music(String title, String composer, Item item, String arranger, BandInPractice bandInPractice, boolean isSuitableForTraining){
        this.item = item;
        this.title = title;
        this.composer = composer;
        this.status = MusicStatus.STORAGE;
        this.arranger = arranger;
        this.bandInPractice =bandInPractice;
        this.isSuitableForTraining = isSuitableForTraining;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getArranger() {
        return arranger;
    }

    public void setArranger(String arranger) {
        this.arranger = arranger;
    }

    public MusicStatus getStatus() {
        return status;
    }

    public void setStatus(MusicStatus status) {
        this.status = status;
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

    public void setSuitableForTraining(boolean isSuitableForTraining) {
        this.isSuitableForTraining = isSuitableForTraining;
    }
}
