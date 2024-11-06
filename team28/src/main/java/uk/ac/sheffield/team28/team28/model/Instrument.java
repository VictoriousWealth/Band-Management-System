package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Instrument")
public class Instrument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SerialNumber")
    private String serialNumber;

    //Getters and setters
    public Instrument() {}

    public Instrument(String serialNumber){
        this.serialNumber = serialNumber;
    }
    public Long getId() {
        return id;
    }

    public String getSerialNumber(){
        return serialNumber;
    }
}
